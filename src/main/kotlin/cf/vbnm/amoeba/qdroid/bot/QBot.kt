package cf.vbnm.amoeba.qdroid.bot

import cf.vbnm.amoeba.core.log.Slf4kt
import cf.vbnm.amoeba.qdroid.bot.plugin.EventPluginManager
import cf.vbnm.amoeba.qdroid.bot.statistics.Statistics
import cf.vbnm.amoeba.qdroid.cq.MessageDetail
import cf.vbnm.amoeba.qdroid.cq.api.BaseApi
import cf.vbnm.amoeba.qdroid.cq.api.data.*
import cf.vbnm.amoeba.qdroid.cq.api.enums.Action
import cf.vbnm.amoeba.qdroid.cq.events.BasePostEvent
import com.fasterxml.jackson.databind.ObjectMapper
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withTimeout
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import java.io.IOException
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicInteger

private val log = Slf4kt.getLogger(QBot::class.java)

class QBot(
    val selfId: Long, private val objectMapper: ObjectMapper, private val eventPluginManager: EventPluginManager
) : SendApi {
    @Volatile
    private lateinit var _webSocketSession: WebSocketSession
    private val resumeQueue = ConcurrentHashMap<Int, Pair<Class<BaseApi<*>>, CancellableContinuation<BaseApi<*>>>>()
    private val seq = AtomicInteger(0)
    val statistics = Statistics()

    var webSocketSession: WebSocketSession
        get() = _webSocketSession
        set(wsSession) {
            if (!this::_webSocketSession.isInitialized) this._webSocketSession = wsSession
            else if (this._webSocketSession !== wsSession) this._webSocketSession = wsSession
        }

    fun handleEvent(event: BasePostEvent) {
        log.info("Handle Event: {}", event.javaClass)
        statistics.addReceivedEvent(event)
        eventPluginManager.handleEvent(event, this)
    }

    fun handleApi(map: Map<String, Any?>) {
        log.debug("handle api: {}", map)
        map["echo"]?.toString()?.split(':')?.get(1)?.let { seq ->
            resumeQueue.remove(seq.toInt())?.let {
                try {
                    val baseApi = objectMapper.convertValue(map, it.first)
                    statistics.addSuccessApi()
                    it.second.resumeWith(Result.success(baseApi))
                } catch (e: Exception) {
                    statistics.addFailedApi()
                    it.second.resumeWith(Result.failure(e))
                }

            }
        }
    }

    private fun generateEchoMsg(seq: Int): String {
        return "$selfId:${seq}"
    }

    private fun newSeq(): Int {
        return seq.incrementAndGet()
    }

    private suspend inline fun <reified R : BaseApi<*>, T : Action<R, *>> send(
        wsSession: WebSocketSession, action: T, vararg params: Pair<String, Any?>
    ): R {
        val seq = newSeq()
        val paramMap: MutableMap<String, Any?> = HashMap()
        params.forEach { it.second?.let { _ -> paramMap[it.first] = it.second } }

        val sendData = SendData(action, paramMap, generateEchoMsg(seq))
        if (!wsSession.isOpen) {
            throw IOException("Bot session has expired.")
        }

        // 防止在没有接收到返回值的情况下造成内存泄漏, 1分钟后将其移除
        val s = objectMapper.writeValueAsString(sendData)
        log.debug("Send Message: {}", s)

        try {
            val r = withTimeout(60000) {
                suspendCancellableCoroutine<R> {
                    @Suppress("UNCHECKED_CAST") val continuation = it as CancellableContinuation<BaseApi<*>>
                    @Suppress("UNCHECKED_CAST") val clazz = R::class.java as Class<BaseApi<*>>
                    resumeQueue[seq] = Pair(clazz, continuation)
                    statistics.addSentApi()
                    wsSession.sendMessage(TextMessage(s))
                }
            }
            return r
        } finally {
            resumeQueue.remove(seq)
        }
    }

    class SendData(
        action: Action<*, *>, params: Map<String, Any?>, val echo: String?
    ) : HashMap<String, Any?>(3) {
        init {
            put("action", action)
            put("params", params)
            echo?.let { put("echo", echo) }
        }
    }

    override suspend fun getLoginInfo(): GetLoginInfo {
        return send(_webSocketSession, Action.GET_LOGIN_INFO)
    }

    override suspend fun setQqProfile(
        nickname: String,
        company: String,
        email: String,
        college: String,
        personalNote: String,
    ): NoData {
        return send(_webSocketSession, Action.SET_QQ_PROFILE)
    }

    override suspend fun qidianGetAccountInfo(): GetQidianInfo {
        return send(_webSocketSession, Action.GET_QIDIAN_ACCOUNT_INFO)
    }

    override suspend fun getModelShow(model: String): GetModelShow {
        return send(_webSocketSession, Action.GET_MODEL_SHOW, Pair("model", model))
    }

    override suspend fun setModelShow(model: String, modelShow: String): NoData {
        return send(_webSocketSession, Action.SET_MODEL_SHOW, Pair("model", model), Pair("model_show", modelShow))
    }

    override suspend fun getOnlineClients(noCache: Boolean): GetOnlineClients {
        return send(_webSocketSession, Action.GET_ONLINE_CLIENTS, Pair("no_cache", noCache))
    }

    override suspend fun getStrangerInfo(userId: Long, noCache: Boolean): GetStrangerInfo {
        return send(
            _webSocketSession, Action.GET_STRANGER_INFO, Pair("user_id", userId), Pair("no_cache", noCache)
        )
    }

    override suspend fun getFriendList(): GetFriendList {
        return send(_webSocketSession, Action.GET_FRIEND_LIST)
    }

    override suspend fun getUnidirectionalFriendList(): GetUnidirectionalFriendList {
        return send(_webSocketSession, Action.GET_UNIDIRECTIONAL_FRIEND_LIST)
    }

    override suspend fun deleteFriend(userId: Long): NoData {
        return send(_webSocketSession, Action.DELETE_FRIEND, Pair("user_id", userId))
    }

    override suspend fun deleteUnidirectionalFriend(): NoData {
        return send(_webSocketSession, Action.DELETE_UNIDIRECTIONAL_FRIEND)
    }

    override suspend fun sendPrivateMsg(
        userId: Long,
        groupId: Long?,
        message: MessageDetail,
        autoEscape: Boolean,
    ): MessageIdRet {
        return send(
            _webSocketSession,
            Action.SEND_PRIVATE_MSG,
            Pair("user_id", userId),
            Pair("group_id", groupId),
            Pair("message", message),
            Pair("auto_escape", autoEscape)
        )
    }

    override suspend fun sendGroupMsg(
        groupId: Long,
        message: MessageDetail,
        autoEscape: Boolean,
    ): MessageIdRet {
        return send(
            _webSocketSession,
            Action.SEND_GROUP_MSG,
            Pair("group_id", groupId),
            Pair("message", message),
            Pair("auto_escape", autoEscape)
        )
    }

    override suspend fun getMsg(messageId: Int): GetMsg {
        return send(_webSocketSession, Action.GET_MSG, Pair("message_id", messageId))
    }

    override suspend fun deleteMsg(messageId: Int): NoData {
        return send(_webSocketSession, Action.DELETE_MSG, Pair("message_id", messageId))
    }

    override suspend fun markMsgAsRead(messageId: Int): NoData {
        return send(_webSocketSession, Action.MARK_MSG_AS_READ, Pair("message_id", messageId))
    }

    override suspend fun getForwardMsg(forwardMessageId: String): GetForwardMsg {
        return send(_webSocketSession, Action.GET_FORWARD_MSG, Pair("forward_message_id", forwardMessageId))
    }

    override suspend fun getGroupMsgHistory(messageSeq: Long?, groupId: Long): GetGroupMsgHistory {
        return send(
            _webSocketSession, Action.GET_GROUP_MSG_HISTORY, Pair("message_seq", messageSeq), Pair("group_id", groupId)
        )
    }

    override suspend fun getImage(file: String): GetImage {
        return send(_webSocketSession, Action.GET_IMAGE, Pair("file", file))
    }

    override suspend fun canSendImage(): CanDo {
        return send(_webSocketSession, Action.CAN_SEND_IMAGE)
    }

    override suspend fun canSendRecord(): CanDo {
        return send(_webSocketSession, Action.CAN_SEND_RECORD)
    }

    override suspend fun setFriendAddRequest(
        flag: String,
        approve: Boolean,
        remark: String,
    ): NoData {
        return send(
            _webSocketSession,
            Action.SET_FRIEND_ADD_REQUEST,
            Pair("flag", flag),
            Pair("approve", approve),
            Pair("remark", remark)
        )
    }

    /**
     * 加群请求审批
     * */
    override suspend fun setGroupAddRequest(
        flag: String,
        subType: String,
        approve: Boolean,
        reason: String,
    ): NoData {
        return send(
            _webSocketSession,
            Action.SET_GROUP_ADD_REQUEST,
            Pair("flag", flag),
            Pair("sub_type", subType),
            Pair("approve", approve),
            Pair("reason", reason)
        )
    }

    override suspend fun getGroupInfo(groupId: Long, noCache: Boolean): GetGroupInfo {
        return send(
            _webSocketSession, Action.GET_GROUP_INFO, Pair("group_id", groupId), Pair("no_cache", noCache)
        )
    }

    override suspend fun getGroupList(noCache: Boolean): GetGroupList {
        return send(
            _webSocketSession, Action.GET_GROUP_LIST, Pair("no_cache", noCache)
        )
    }

    override suspend fun getGroupMemberInfo(
        groupId: Long,
        userId: Long,
        noCache: Boolean,
    ): GetGroupMemberInfo {
        return send(
            _webSocketSession,
            Action.GET_GROUP_MEMBER_INFO,
            Pair("group_id", groupId),
            Pair("user_id", userId),
            Pair("no_cache", noCache)
        )
    }

    override suspend fun getGroupMemberList(groupId: Long, noCache: Boolean): GetGroupMemberList {
        return send(
            _webSocketSession, Action.GET_GROUP_MEMBER_LIST, Pair("group_id", groupId), Pair("no_cache", noCache)
        )
    }

    override suspend fun getGroupHonorInfo(groupId: Long, type: String): GetGroupHonorInfo {
        return send(
            _webSocketSession, Action.GET_GROUP_HONOR_INFO, Pair("group_id", groupId), Pair("type", type)
        )
    }

    override suspend fun getEssenceMsgList(groupId: Long): GetEssenceMsgList {
        return send(_webSocketSession, Action.GET_ESSENCE_MSG_LIST, Pair("group_id", groupId))
    }

    override suspend fun getGroupAtAllRemain(groupId: Long): GetGroupAtAllRemain {
        return send(_webSocketSession, Action.GET_GROUP_AT_ALL_REMAIN, Pair("group_id", groupId))
    }

    override suspend fun setGroupName(groupId: Long, groupName: String): NoData {
        return send(
            _webSocketSession, Action.SET_GROUP_NAME, Pair("group_id", groupId), Pair("group_name", groupName)
        )
    }

    override suspend fun setGroupPortrait(groupId: Long, file: String, cache: Int): NoData {
        return send(
            _webSocketSession,
            Action.SET_GROUP_PORTRAIT,
            Pair("group_id", groupId),
            Pair("file", file),
            Pair("cache", cache)
        )
    }

    override suspend fun setGroupAdmin(groupId: Long, userId: Long, enable: Boolean): NoData {
        return send(
            _webSocketSession,
            Action.SET_GROUP_ADMIN,
            Pair("group_id", groupId),
            Pair("user_id", userId),
            Pair("enable", enable)
        )
    }

    override suspend fun setGroupCard(groupId: Long, userId: Long, card: String): NoData {
        return send(
            _webSocketSession,
            Action.SET_GROUP_CARD,
            Pair("group_id", groupId),
            Pair("user_id", userId),
            Pair("card", card)
        )
    }

    override suspend fun setGroupSpecialTitle(
        groupId: Long,
        userId: Long,
        specialTitle: String,
        duration: Int,
    ): NoData {
        return send(
            _webSocketSession,
            Action.SET_GROUP_SPECIAL_TITLE,
            Pair("group_id", groupId),
            Pair("user_id", userId),
            Pair("special_title", specialTitle),
            Pair("duration", duration)
        )
    }

    override suspend fun setGroupBan(groupId: Long, userId: Long, duration: Int): NoData {
        return send(
            _webSocketSession,
            Action.SET_GROUP_BAN,
            Pair("group_id", groupId),
            Pair("user_id", userId),
            Pair("duration", duration)
        )
    }

    override suspend fun setGroupWholeBan(groupId: Long, enable: Boolean): NoData {
        return send(
            _webSocketSession, Action.SET_GROUP_WHOLE_BAN, Pair("group_id", groupId), Pair("enable", enable)
        )
    }

    override suspend fun setGroupAnonymousBan(
        groupId: Long,
        anonymous: Any?,
        anonymousFlag: String?,
        duration: Int,
    ): NoData {
        return send(
            _webSocketSession,
            Action.SET_GROUP_ANONYMOUS_BAN,
            Pair("group_id", groupId),
            Pair("anonymous", anonymous),
            Pair("anonymous_flag", anonymousFlag),
            Pair("duration", duration)
        )
    }

    override suspend fun setEssenceMsg(messageId: Long): NoData {
        return send(_webSocketSession, Action.SET_ESSENCE_MSG, Pair("message_id", messageId))
    }

    override suspend fun sendGroupSign(groupId: Long): NoData {
        return send(_webSocketSession, Action.SEND_GROUP_SIGN, Pair("group_id", groupId))
    }

    override suspend fun sendGroupNotice(groupId: Long, content: String, image: String?): NoData {
        return send(
            _webSocketSession,
            Action.SEND_GROUP_NOTICE,
            Pair("group_id", groupId),
            Pair("content", content),
            Pair("image", image)
        )
    }

    override suspend fun getGroupNotice(groupId: Long): GetGroupNotice {
        return send(_webSocketSession, Action.GET_GROUP_NOTICE, Pair("group_id", groupId))
    }

    override suspend fun setGroupKick(
        groupId: Long,
        userId: Long,
        rejectAddRequest: Boolean,
    ): NoData {
        return send(
            _webSocketSession,
            Action.SET_GROUP_KICK,
            Pair("group_id", groupId),
            Pair("user_id", userId),
            Pair("reject_add_request", rejectAddRequest)
        )
    }

    override suspend fun setGroupLeave(groupId: Long, isDismiss: Boolean): NoData {
        return send(
            _webSocketSession, Action.SET_GROUP_LEAVE, Pair("group_id", groupId), Pair("is_dismiss", isDismiss)
        )
    }

    override suspend fun uploadGroupFile(
        groupId: Long,
        file: String,
        name: String,
        folder: String,
    ): NoData {
        return send(
            _webSocketSession,
            Action.UPLOAD_GROUP_FILE,
            Pair("group_id", groupId),
            Pair("file", file),
            Pair("name", name),
            Pair("folder", folder)
        )
    }

    override suspend fun deleteGroupFile(groupId: Long, fileId: String, busid: Int): NoData {
        return send(
            _webSocketSession,
            Action.DELETE_GROUP_FILE,
            Pair("group_id", groupId),
            Pair("file_id", fileId),
            Pair("busid", busid)
        )
    }

    override suspend fun createGroupFileFolder(
        groupId: Long,
        name: String,
        parentId: String,
    ): NoData {
        return send(
            _webSocketSession,
            Action.CREATE_GROUP_FILE_FOLDER,
            Pair("group_id", groupId),
            Pair("name", name),
            Pair("parent_id", parentId)
        )
    }

    override suspend fun deleteGroupFolder(groupId: Long, folderId: String): NoData {
        return send(
            _webSocketSession, Action.DELETE_GROUP_FOLDER, Pair("group_id", groupId), Pair("folder_id", folderId)
        )
    }

    override suspend fun getGroupFileSystemInfo(groupId: Long): GetGroupFileSystemInfo {
        return send(_webSocketSession, Action.GET_GROUP_FILE_SYSTEM_INFO, Pair("group_id", groupId))
    }

    override suspend fun getGroupFilesByFolder(
        groupId: Long,
        folderId: String,
    ): GetGroupFilesByFolder {
        return send(
            _webSocketSession, Action.GET_GROUP_FILES_BY_FOLDER, Pair("group_id", groupId), Pair("folder_id", folderId)
        )
    }

    override suspend fun getGroupFileUrl(
        groupId: Long,
        fileId: String,
        busid: Int,
    ): GetGroupFileUrl {
        return send(
            _webSocketSession,
            Action.GET_GROUP_FILE_URL,
            Pair("group_id", groupId),
            Pair("file_id", fileId),
            Pair("busid", busid)
        )
    }

    override suspend fun uploadPrivateFile(userId: Long, file: String, name: String): NoData {
        return send(
            _webSocketSession,
            Action.UPLOAD_PRIVATE_FILE,
            Pair("user_id", userId),
            Pair("file", file),
            Pair("name", name)
        )
    }

    override suspend fun getVersionInfo(): GetVersionInfo {
        return send(_webSocketSession, Action.GET_VERSION_INFO)
    }

    override suspend fun getStatus(): GetStatus {
        return send(_webSocketSession, Action.GET_STATUS)
    }

    override suspend fun reloadEventFilter(file: String): NoData {
        return send(_webSocketSession, Action.RELOAD_EVENT_FILTER, Pair("file", file))
    }

    override suspend fun downloadFile(
        url: String,
        threadCount: Int,
        vararg headers: Pair<String, String>,

        ): DownloadFile {
        val headersList = Array(headers.size) {
            headers[it].first + ": " + headers[it].second
        }
        return send(
            _webSocketSession,
            Action.DOWNLOAD_FILE,
            Pair("url", url),
            Pair("thread_count", threadCount),
            Pair("headers", headersList)
        )
    }

    override suspend fun checkUrlSafely(url: String): CheckUrlSafely {
        return send(_webSocketSession, Action.CHECK_URL_SAFELY, Pair("url", url))
    }

    override suspend fun handleQuickOperation(context: BasePostEvent, operation: Any): NoData {
        return send(
            _webSocketSession, Action.HANDLE_QUICK_OPERATION,
            Pair("context", context),
            Pair("operation", operation),
        )
    }

    override suspend fun sendGuildChannelMsg(
        guildId: Long, channelId: Long, message: MessageDetail
    ): StringMessageIdRet {
        return send(
            _webSocketSession,
            Action.SEND_GUILD_CHANNEL_MSG,
            Pair("guild_id", guildId),
            Pair("channel_id", channelId),
            Pair("message", message)
        )
    }

    override suspend fun getGuildMsg(messageId: String, noCache: Boolean): GetGuildMsg {
        return send(
            _webSocketSession, Action.GET_GUILD_MSG, Pair("message_id", messageId), Pair("no_cache", noCache)
        )
    }
}
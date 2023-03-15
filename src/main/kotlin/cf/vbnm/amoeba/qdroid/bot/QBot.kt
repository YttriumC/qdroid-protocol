package cf.vbnm.amoeba.qdroid.bot

import cf.vbnm.amoeba.core.log.Slf4kt
import cf.vbnm.amoeba.qdroid.bot.job.TimeoutJob
import cf.vbnm.amoeba.qdroid.bot.plugin.PluginManager
import cf.vbnm.amoeba.qdroid.cq.MessageDetail
import cf.vbnm.amoeba.qdroid.cq.api.BaseApi
import cf.vbnm.amoeba.qdroid.cq.api.data.*
import cf.vbnm.amoeba.qdroid.cq.api.enums.Action
import cf.vbnm.amoeba.qdroid.cq.events.BasePostEvent
import com.fasterxml.jackson.databind.ObjectMapper
import org.quartz.Scheduler
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.TimeUnit

private val log = Slf4kt.getLogger(QBot::class.java)

class QBot(
    val selfId: Long,
    private val objectMapper: ObjectMapper,
    private val scheduler: Scheduler,
    private val pluginManager: PluginManager
) :
    SendApi {
    @Volatile
    private lateinit var webSocketSession: WebSocketSession
    private val waitMap =
        ConcurrentHashMap<Int, Triple<Class<BaseApi<*>>, (BaseApi<*>) -> Unit, Pair<String, String>>>()

    @Volatile
    private var seq = 0

    fun setWebSocketSession(wsSession: WebSocketSession) {
        this.webSocketSession = wsSession

    }

    fun handleEvent(event: BasePostEvent) {
        log.info("Handle Event: {}", event.javaClass)
        pluginManager.handleEvent(event, this)
    }

    fun handleApi(map: Map<String, Any?>) {
        log.info("handle api: {}", map)
        map["echo"]?.toString()?.split(':')?.get(1)?.let { seq ->
//            CleanBotWaitList.addTimeoutTrigger(scheduler, this, it.toInt(), waitMap)
            waitMap.remove(seq.toInt())?.let {
                it.second.invoke(objectMapper.convertValue(map, it.first))
                TimeoutJob.cancelTimeoutJob(scheduler, it.third)
            }
        }
    }

    private fun generateEchoMsg(seq: Int): String {
        return "$selfId:${seq}"
    }

    @Synchronized
    private fun newSeq(): Int {
        return seq++
    }

    private inline fun <reified R : BaseApi<*>, T : Action<R>> send(
        wsSession: WebSocketSession,
        action: T,
        noinline callback: (R) -> Unit,
        vararg params: Pair<String, Any?>
    ) {
        val seq = newSeq()
        val paramMap: MutableMap<String, Any?>
        paramMap = HashMap()
        params.forEach { it.second?.let { _ -> paramMap[it.first] = it.second } }

        val sendData = SendData(action, paramMap, generateEchoMsg(seq))
        if (!wsSession.isOpen) {
            return
        }
        val s = objectMapper.writeValueAsString(sendData)
        log.debug("Send Message: {}", s)
        wsSession.sendMessage(TextMessage(s))
        //等待60秒
        //TODO 异步获取, 异步handle api返回值

        TimeoutJob.addTimeoutJob(scheduler, 1, TimeUnit.MINUTES) {
            waitMap.remove(seq)
        }.also {
            @Suppress("UNCHECKED_CAST")
            waitMap[seq] = Triple(
                R::class.java, callback, it
            ) as Triple<Class<BaseApi<*>>, (BaseApi<*>) -> Unit, Pair<String, String>>
        }
    }

    class SendData(
        action: Action<*>,
        params: Map<String, Any?>,
        val echo: String?
    ) : HashMap<String, Any?>(3) {
        init {
            put("action", action)
            put("params", params)
            echo?.let { put("echo", echo) }
        }
    }

    override fun getLoginInfo(callback: (GetLoginInfo) -> Unit) {
        send(webSocketSession, Action.GET_LOGIN_INFO, callback)
    }

    override fun setQqProfile(
        nickname: String,
        company: String,
        email: String,
        college: String,
        personalNote: String,
        callback: (NoData) -> Unit
    ) {
        send(webSocketSession, Action.SET_QQ_PROFILE, callback)
    }

    override fun qidianGetAccountInfo(callback: (GetQidianInfo) -> Unit) {
        send(webSocketSession, Action.GET_QIDIAN_ACCOUNT_INFO, callback)
    }

    override fun getModelShow(model: String, callback: (GetModelShow) -> Unit) {
        send(webSocketSession, Action.GET_MODEL_SHOW, callback, Pair("model", model))
    }

    override fun setModelShow(model: String, modelShow: String, callback: (NoData) -> Unit) {
        send(webSocketSession, Action.SET_MODEL_SHOW, callback, Pair("model", model), Pair("model_show", modelShow))
    }

    override fun getOnlineClients(noCache: Boolean, callback: (GetOnlineClients) -> Unit) {
        send(webSocketSession, Action.GET_ONLINE_CLIENTS, callback, Pair("no_cache", noCache))
    }

    override fun getStrangerInfo(userId: Long, noCache: Boolean, callback: (GetStrangerInfo) -> Unit) {
        send(webSocketSession, Action.GET_STRANGER_INFO, callback, Pair("user_id", userId), Pair("no_cache", noCache))
    }

    override fun getFriendList(callback: (GetFriendList) -> Unit) {
        send(webSocketSession, Action.GET_FRIEND_LIST, callback)
    }

    override fun getUnidirectionalFriendList(callback: (GetUnidirectionalFriendList) -> Unit) {
        send(webSocketSession, Action.GET_UNIDIRECTIONAL_FRIEND_LIST, callback)
    }

    override fun deleteFriend(userId: Long, callback: (NoData) -> Unit) {
        send(webSocketSession, Action.DELETE_FRIEND, callback, Pair("user_id", userId))
    }

    override fun deleteUnidirectionalFriend(callback: (NoData) -> Unit) {
        send(webSocketSession, Action.DELETE_UNIDIRECTIONAL_FRIEND, callback)
    }

    override fun sendPrivateMsg(
        userId: Long,
        groupId: Long?,
        message: MessageDetail,
        autoEscape: Boolean,
        callback: (MessageIdRet) -> Unit
    ) {
        send(
            webSocketSession, Action.SEND_PRIVATE_MSG, callback,
            Pair("user_id", userId),
            Pair("group_id", groupId),
            Pair("message", message),
            Pair("auto_escape", autoEscape)
        )
    }

    override fun sendGroupMsg(
        groupId: Long,
        message: MessageDetail,
        autoEscape: Boolean,
        callback: (MessageIdRet) -> Unit
    ) {
        send(
            webSocketSession, Action.SEND_GROUP_MSG, callback,
            Pair("group_id", groupId),
            Pair("message", message), Pair("auto_escape", autoEscape)
        )
    }

    override fun getMsg(messageId: Long, callback: (GetMsg) -> Unit) {
        send(webSocketSession, Action.GET_MSG, callback, Pair("message_id", messageId))
    }

    override fun deleteMsg(messageId: Long, callback: (NoData) -> Unit) {
        send(webSocketSession, Action.DELETE_MSG, callback, Pair("message_id", messageId))
    }

    override fun markMsgAsRead(messageId: Long, callback: (NoData) -> Unit) {
        send(webSocketSession, Action.MARK_MSG_AS_READ, callback, Pair("message_id", messageId))
    }

    override fun getForwardMsg(forwardMessageId: String, callback: (GetForwardMsg) -> Unit) {
        send(webSocketSession, Action.GET_FORWARD_MSG, callback, Pair("forward_message_id", forwardMessageId))
    }

    override fun getGroupMsgHistory(messageSeq: Long?, groupId: Long, callback: (GetGroupMsgHistory) -> Unit) {
        send(
            webSocketSession, Action.GET_GROUP_MSG_HISTORY, callback, Pair("message_seq", messageSeq),
            Pair("group_id", groupId)
        )
    }

    override fun getImage(file: String, callback: (GetImage) -> Unit) {
        send(webSocketSession, Action.GET_IMAGE, callback, Pair("file", file))
    }

    override fun canSendImage(callback: (CanDo) -> Unit) {
        send(webSocketSession, Action.CAN_SEND_IMAGE, callback)
    }

    override fun canSendRecord(callback: (CanDo) -> Unit) {
        send(webSocketSession, Action.CAN_SEND_RECORD, callback)
    }

    override fun setFriendAddRequest(flag: String, approve: Boolean, remark: String, callback: (NoData) -> Unit) {
        send(
            webSocketSession, Action.SET_FRIEND_ADD_REQUEST, callback,
            Pair("flag", flag), Pair("approve", approve),
            Pair("remark", remark)
        )
    }

    /**
     * 加群请求审批
     * */
    override fun setGroupAddRequest(
        flag: String,
        subType: String,
        approve: Boolean,
        reason: String,
        callback: (NoData) -> Unit
    ) {
        send(
            webSocketSession, Action.SET_GROUP_ADD_REQUEST, callback,
            Pair("flag", flag), Pair("sub_type", subType),
            Pair("approve", approve),
            Pair("reason", reason)
        )
    }

    override fun getGroupInfo(groupId: Long, noCache: Boolean, callback: (GetGroupInfo) -> Unit) {
        send(webSocketSession, Action.GET_GROUP_INFO, callback, Pair("group_id", groupId), Pair("no_cache", noCache))
    }

    override fun getGroupList(noCache: Boolean, callback: (GetGroupList) -> Unit) {
        send(
            webSocketSession, Action.GET_GROUP_LIST, callback,
            Pair("no_cache", noCache)
        )
    }

    override fun getGroupMemberInfo(
        groupId: Long,
        userId: Long,
        noCache: Boolean,
        callback: (GetGroupMemberInfo) -> Unit
    ) {
        send(
            webSocketSession, Action.GET_GROUP_MEMBER_INFO, callback,
            Pair("group_id", groupId),
            Pair("user_id", userId),
            Pair("no_cache", noCache)
        )
    }

    override fun getGroupMemberList(groupId: Long, noCache: Boolean, callback: (GetGroupMemberList) -> Unit) {
        send(
            webSocketSession, Action.GET_GROUP_MEMBER_LIST, callback,
            Pair("group_id", groupId), Pair("no_cache", noCache)
        )
    }

    override fun getGroupHonorInfo(groupId: Long, type: String, callback: (GetGroupHonorInfo) -> Unit) {
        send(
            webSocketSession, Action.GET_GROUP_HONOR_INFO, callback,
            Pair("group_id", groupId),
            Pair("type", type)
        )
    }

    override fun getEssenceMsgList(groupId: Long, callback: (GetEssenceMsgList) -> Unit) {
        send(webSocketSession, Action.GET_ESSENCE_MSG_LIST, callback, Pair("group_id", groupId))
    }

    override fun getGroupAtAllRemain(groupId: Long, callback: (GetGroupAtAllRemain) -> Unit) {
        send(webSocketSession, Action.GET_GROUP_AT_ALL_REMAIN, callback, Pair("group_id", groupId))
    }

    override fun setGroupName(groupId: Long, groupName: String, callback: (NoData) -> Unit) {
        send(
            webSocketSession,
            Action.SET_GROUP_NAME,
            callback,
            Pair("group_id", groupId),
            Pair("group_name", groupName)
        )
    }

    override fun setGroupPortrait(groupId: Long, file: String, cache: Int, callback: (NoData) -> Unit) {
        send(
            webSocketSession, Action.SET_GROUP_PORTRAIT, callback,
            Pair("group_id", groupId),
            Pair("file", file), Pair("cache", cache)
        )
    }

    override fun setGroupAdmin(groupId: Long, userId: Long, enable: Boolean, callback: (NoData) -> Unit) {
        send(
            webSocketSession,
            Action.SET_GROUP_ADMIN, callback,
            Pair("group_id", groupId),
            Pair("user_id", userId),
            Pair("enable", enable)
        )
    }

    override fun setGroupCard(groupId: Long, userId: Long, card: String, callback: (NoData) -> Unit) {
        send(
            webSocketSession, Action.SET_GROUP_CARD, callback, Pair("group_id", groupId),
            Pair("user_id", userId),
            Pair("card", card)
        )
    }

    override fun setGroupSpecialTitle(
        groupId: Long,
        userId: Long,
        specialTitle: String,
        duration: Int,
        callback: (NoData) -> Unit
    ) {
        send(
            webSocketSession, Action.SET_GROUP_SPECIAL_TITLE, callback,
            Pair("group_id", groupId),
            Pair("user_id", userId),
            Pair("special_title", specialTitle),
            Pair("duration", duration)
        )
    }

    override fun setGroupBan(groupId: Long, userId: Long, duration: Int, callback: (NoData) -> Unit) {
        send(
            webSocketSession, Action.SET_GROUP_BAN, callback,
            Pair("group_id", groupId), Pair("user_id", userId), Pair("duration", duration)
        )
    }

    override fun setGroupWholeBan(groupId: Long, enable: Boolean, callback: (NoData) -> Unit) {
        send(
            webSocketSession,
            Action.SET_GROUP_WHOLE_BAN, callback, Pair("group_id", groupId), Pair("enable", enable)
        )
    }

    override fun setGroupAnonymousBan(
        groupId: Long,
        anonymous: Any?,
        anonymousFlag: String?,
        duration: Int,
        callback: (NoData) -> Unit
    ) {
        send(
            webSocketSession, Action.SET_GROUP_ANONYMOUS_BAN, callback,
            Pair("group_id", groupId),
            Pair("anonymous", anonymous),
            Pair("anonymous_flag", anonymousFlag),
            Pair("duration", duration)
        )
    }

    override fun setEssenceMsg(messageId: Long, callback: (NoData) -> Unit) {
        send(webSocketSession, Action.SET_ESSENCE_MSG, callback, Pair("message_id", messageId))
    }

    override fun sendGroupSign(groupId: Long, callback: (NoData) -> Unit) {
        send(webSocketSession, Action.SEND_GROUP_SIGN, callback, Pair("group_id", groupId))
    }

    override fun sendGroupNotice(groupId: Long, content: String, image: String?, callback: (NoData) -> Unit) {
        send(
            webSocketSession, Action.SEND_GROUP_NOTICE, callback,
            Pair("group_id", groupId),
            Pair("content", content), Pair("image", image)
        )
    }

    override fun getGroupNotice(groupId: Long, callback: (GetGroupNotice) -> Unit) {
        send(webSocketSession, Action.GET_GROUP_NOTICE, callback, Pair("group_id", groupId))
    }

    override fun setGroupKick(groupId: Long, userId: Long, rejectAddRequest: Boolean, callback: (NoData) -> Unit) {
        send(
            webSocketSession, Action.SET_GROUP_KICK, callback,
            Pair("group_id", groupId),
            Pair("user_id", userId),
            Pair("reject_add_request", rejectAddRequest)
        )
    }

    override fun setGroupLeave(groupId: Long, isDismiss: Boolean, callback: (NoData) -> Unit) {
        send(
            webSocketSession,
            Action.SET_GROUP_LEAVE,
            callback,
            Pair("group_id", groupId),
            Pair("is_dismiss", isDismiss)
        )
    }

    override fun uploadGroupFile(
        groupId: Long,
        file: String,
        name: String,
        folder: String,
        callback: (NoData) -> Unit
    ) {
        send(
            webSocketSession, Action.UPLOAD_GROUP_FILE, callback, Pair("group_id", groupId),
            Pair("file", file), Pair("name", name), Pair("folder", folder)
        )
    }

    override fun deleteGroupFile(groupId: Long, fileId: String, busid: Int, callback: (NoData) -> Unit) {
        send(
            webSocketSession, Action.DELETE_GROUP_FILE, callback,
            Pair("group_id", groupId), Pair("file_id", fileId),
            Pair("busid", busid)
        )
    }

    override fun createGroupFileFolder(groupId: Long, name: String, parentId: String, callback: (NoData) -> Unit) {
        send(
            webSocketSession, Action.CREATE_GROUP_FILE_FOLDER, callback,
            Pair("group_id", groupId),
            Pair("name", name),
            Pair("parent_id", parentId)
        )
    }

    override fun deleteGroupFolder(groupId: Long, folderId: String, callback: (NoData) -> Unit) {
        send(
            webSocketSession,
            Action.DELETE_GROUP_FOLDER, callback,
            Pair("group_id", groupId),
            Pair("folder_id", folderId)
        )
    }

    override fun getGroupFileSystemInfo(groupId: Long, callback: (GetGroupFileSystemInfo) -> Unit) {
        send(webSocketSession, Action.GET_GROUP_FILE_SYSTEM_INFO, callback, Pair("group_id", groupId))
    }

    override fun getGroupFilesByFolder(groupId: Long, folderId: String, callback: (GetGroupFilesByFolder) -> Unit) {
        send(
            webSocketSession, Action.GET_GROUP_FILES_BY_FOLDER, callback,
            Pair("group_id", groupId), Pair("folder_id", folderId)
        )
    }

    override fun getGroupFileUrl(groupId: Long, fileId: String, busid: Int, callback: (GetGroupFileUrl) -> Unit) {
        send(
            webSocketSession, Action.GET_GROUP_FILE_URL, callback, Pair("group_id", groupId),
            Pair("file_id", fileId),
            Pair("busid", busid)
        )
    }

    override fun uploadPrivateFile(userId: Long, file: String, name: String, callback: (NoData) -> Unit) {
        send(
            webSocketSession, Action.UPLOAD_PRIVATE_FILE, callback,
            Pair("user_id", userId),
            Pair("file", file),
            Pair("name", name)
        )
    }

    override fun getVersionInfo(callback: (GetVersionInfo) -> Unit) {
        send(webSocketSession, Action.GET_VERSION_INFO, callback)
    }

    override fun getStatus(callback: (GetStatus) -> Unit) {
        send(webSocketSession, Action.GET_STATUS, callback)
    }

    override fun reloadEventFilter(file: String, callback: (NoData) -> Unit) {
        send(webSocketSession, Action.RELOAD_EVENT_FILTER, callback, Pair("file", file))
    }

    override fun downloadFile(
        url: String,
        threadCount: Int,
        vararg headers: Pair<String, String>,
        callback: (DownloadFile) -> Unit
    ) {
        val headersList = Array(headers.size) {
            headers[it].first + ": " + headers[it].second
        }
        send(
            webSocketSession, Action.DOWNLOAD_FILE, callback,
            Pair("url", url),
            Pair("thread_count", threadCount),
            Pair("headers", headersList)
        )
    }

    override fun checkUrlSafely(url: String, callback: (CheckUrlSafely) -> Unit) {
        send(webSocketSession, Action.CHECK_URL_SAFELY, callback, Pair("url", url))
    }

    override fun handleQuickOperation(context: BasePostEvent, operation: Any, callback: (NoData) -> Unit) {
        send(
            webSocketSession, Action.HANDLE_QUICK_OPERATION, callback,
            Pair("context", context),
            Pair("operation", operation),
        )
    }
}
package cf.vbnm.amoeba.qdroid.bot

import cf.vbnm.amoeba.qdroid.cq.MessageDetail
import cf.vbnm.amoeba.qdroid.cq.api.data.*
import cf.vbnm.amoeba.qdroid.cq.api.enums.Retcode
import cf.vbnm.amoeba.qdroid.cq.api.enums.Status
import cf.vbnm.amoeba.qdroid.cq.events.BasePostEvent
import cf.vbnm.amoeba.qdroid.cq.events.enums.PostMessageType

interface SendApi {

    suspend fun getLoginInfo(): GetLoginInfo

    suspend fun setQqProfile(
        nickname: String,
        company: String,
        email: String,
        college: String,
        personalNote: String,
    ): NoData

    suspend fun qidianGetAccountInfo(): GetQidianInfo

    suspend fun getModelShow(model: String): GetModelShow

    suspend fun setModelShow(model: String, modelShow: String): NoData

    suspend fun getOnlineClients(noCache: Boolean): GetOnlineClients

    suspend fun getStrangerInfo(userId: Long, noCache: Boolean = false): GetStrangerInfo

    suspend fun getFriendList(): GetFriendList

    suspend fun getUnidirectionalFriendList(): GetUnidirectionalFriendList

    suspend fun deleteFriend(userId: Long): NoData

    suspend fun deleteUnidirectionalFriend(): NoData

    suspend fun sendPrivateMsg(
        userId: Long,
        groupId: Long? = null,
        message: MessageDetail,
        autoEscape: Boolean = false,
    ): MessageIdRet

    suspend fun sendGroupMsg(
        groupId: Long, message: MessageDetail, autoEscape: Boolean = false,
    ): MessageIdRet

    suspend fun sendMsg(
        messageType: PostMessageType? = null,
        userId: Long?,
        groupId: Long?,
        message: MessageDetail,
        autoEscape: Boolean = false,
    ): MessageIdRet {
        if (userId == null && groupId == null) {
            throw NullPointerException("Both of UserId and GroupId cannot be null")
        }
        try {
            messageType?.let {
                return when (it) {
                    PostMessageType.PRIVATE -> sendPrivateMsg(userId!!, groupId, message, autoEscape)
                    PostMessageType.GROUP -> sendGroupMsg(groupId!!, message, autoEscape)
                    PostMessageType.GUILD -> MessageIdRet(
                        Status.FAILED, Retcode.FAILED, "频道消息不支持", null, null, MessageIdRet.MessageId(0)
                    )
                }
            }
        } catch (e: NullPointerException) {
            throw IllegalArgumentException("Message type cannot match the user id and group id")
        }
        userId?.let {
            return sendPrivateMsg(userId, groupId, message, autoEscape)
        }
        groupId?.let {
            return sendGroupMsg(groupId, message, autoEscape)
        }
        throw IllegalStateException("Unknown Exception")
    }

    suspend fun getMsg(messageId: Int): GetMsg

    suspend fun deleteMsg(messageId: Int): NoData

    suspend fun markMsgAsRead(messageId: Int): NoData

    suspend fun getForwardMsg(forwardMessageId: String): GetForwardMsg

//    suspend fun sendGroupForwardMsg(groupId: Long,messages:List<MessagePartial>)

    suspend fun getGroupMsgHistory(messageSeq: Long? = null, groupId: Long): GetGroupMsgHistory

    suspend fun getImage(file: String): GetImage

    suspend fun canSendImage(): CanDo

//    suspend fun ocrImage(image:String):

    suspend fun canSendRecord(): CanDo

    suspend fun setFriendAddRequest(flag: String, approve: Boolean = true, remark: String): NoData

    suspend fun setGroupAddRequest(
        flag: String, subType: String, approve: Boolean = true, reason: String = "",
    ): NoData

    suspend fun getGroupInfo(groupId: Long, noCache: Boolean = false): GetGroupInfo

    suspend fun getGroupList(noCache: Boolean = false): GetGroupList

    suspend fun getGroupMemberInfo(
        groupId: Long, userId: Long, noCache: Boolean = false,
    ): GetGroupMemberInfo

    suspend fun getGroupMemberList(groupId: Long, noCache: Boolean = false): GetGroupMemberList

    suspend fun getGroupHonorInfo(groupId: Long, type: String): GetGroupHonorInfo

    //    suspend fun getGroupSystemMsg():GetGroup
    suspend fun getEssenceMsgList(groupId: Long): GetEssenceMsgList

    suspend fun getGroupAtAllRemain(groupId: Long): GetGroupAtAllRemain

    suspend fun setGroupName(groupId: Long, groupName: String): NoData

    suspend fun setGroupPortrait(groupId: Long, file: String, cache: Int = 1): NoData

    suspend fun setGroupAdmin(groupId: Long, userId: Long, enable: Boolean = true): NoData

    suspend fun setGroupCard(groupId: Long, userId: Long, card: String = ""): NoData

    suspend fun setGroupSpecialTitle(
        groupId: Long, userId: Long, specialTitle: String = "", duration: Int = -1,
    ): NoData

    suspend fun setGroupBan(groupId: Long, userId: Long, duration: Int = 30 * 60): NoData

    suspend fun setGroupWholeBan(groupId: Long, enable: Boolean = true): NoData

    suspend fun setGroupAnonymousBan(
        groupId: Long, anonymous: Any?, anonymousFlag: String?, duration: Int,
    ): NoData

    suspend fun setEssenceMsg(messageId: Long): NoData

    suspend fun sendGroupSign(groupId: Long): NoData

    suspend fun sendGroupNotice(groupId: Long, content: String, image: String?): NoData

    suspend fun getGroupNotice(groupId: Long): GetGroupNotice

    suspend fun setGroupKick(groupId: Long, userId: Long, rejectAddRequest: Boolean = false): NoData

    suspend fun setGroupLeave(groupId: Long, isDismiss: Boolean = false): NoData

    suspend fun uploadGroupFile(groupId: Long, file: String, name: String, folder: String): NoData

    suspend fun deleteGroupFile(groupId: Long, fileId: String, busid: Int): NoData

    suspend fun createGroupFileFolder(groupId: Long, name: String, parentId: String = "/"): NoData

    suspend fun deleteGroupFolder(groupId: Long, folderId: String): NoData

    suspend fun getGroupFileSystemInfo(groupId: Long): GetGroupFileSystemInfo

    suspend fun getGroupFilesByFolder(groupId: Long, folderId: String): GetGroupFilesByFolder

    suspend fun getGroupFileUrl(groupId: Long, fileId: String, busid: Int): GetGroupFileUrl

    suspend fun uploadPrivateFile(userId: Long, file: String, name: String): NoData

    suspend fun getVersionInfo(): GetVersionInfo

    suspend fun getStatus(): GetStatus

    suspend fun reloadEventFilter(file: String): NoData

    suspend fun downloadFile(
        url: String, threadCount: Int = 4, vararg headers: Pair<String, String>,
    ): DownloadFile

    suspend fun checkUrlSafely(url: String): CheckUrlSafely
//    suspend fun getWordSlices()

    suspend fun handleQuickOperation(context: BasePostEvent, operation: Any): NoData

    suspend fun sendGuildChannelMsg(guildId: Long, channelId: Long, message: MessageDetail): StringMessageIdRet

    suspend fun getGuildMsg(messageId: String, noCache: Boolean = false): GetGuildMsg
}

package cf.vbnm.amoeba.qdroid.bot

import cf.vbnm.amoeba.qdroid.cq.MessageDetail
import cf.vbnm.amoeba.qdroid.cq.api.data.*
import cf.vbnm.amoeba.qdroid.cq.events.BasePostEvent
import cf.vbnm.amoeba.qdroid.cq.events.enums.PostMessageType

interface SendApi {

    fun getLoginInfo(callback: (GetLoginInfo) -> Unit)

    fun setQqProfile(
        nickname: String,
        company: String,
        email: String,
        college: String,
        personalNote: String,
        callback: (NoData) -> Unit
    )

    fun qidianGetAccountInfo(callback: (GetQidianInfo) -> Unit)

    fun getModelShow(model: String, callback: (GetModelShow) -> Unit)

    fun setModelShow(model: String, modelShow: String, callback: (NoData) -> Unit)

    fun getOnlineClients(noCache: Boolean, callback: (GetOnlineClients) -> Unit)

    fun getStrangerInfo(userId: Long, noCache: Boolean = false, callback: (GetStrangerInfo) -> Unit)

    fun getFriendList(callback: (GetFriendList) -> Unit)

    fun getUnidirectionalFriendList(callback: (GetUnidirectionalFriendList) -> Unit)

    fun deleteFriend(userId: Long, callback: (NoData) -> Unit)

    fun deleteUnidirectionalFriend(callback: (NoData) -> Unit)

    fun sendPrivateMsg(
        userId: Long,
        groupId: Long? = null,
        message: MessageDetail,
        autoEscape: Boolean = false,
        callback: (MessageIdRet) -> Unit
    )

    fun sendGroupMsg(
        groupId: Long,
        message: MessageDetail,
        autoEscape: Boolean = false,
        callback: (MessageIdRet) -> Unit
    )

    fun sendMsg(
        messageType: PostMessageType? = null,
        userId: Long?,
        groupId: Long?,
        message: MessageDetail,
        autoEscape: Boolean = false,
        callback: (MessageIdRet) -> Unit
    ) {
        if (userId == null && groupId == null) {
            throw NullPointerException("Both of UserId and GroupId cannot be null")
        }
        try {
            messageType?.let {
                return when (it) {
                    PostMessageType.PRIVATE -> sendPrivateMsg(userId!!, groupId, message, autoEscape, callback)
                    PostMessageType.GROUP -> sendGroupMsg(groupId!!, message, autoEscape, callback)
                }
            }
        } catch (e: NullPointerException) {
            throw IllegalArgumentException("Message type cannot match the user id and group id")
        }
        userId?.let {
            return sendPrivateMsg(userId, groupId, message, autoEscape, callback)
        }
        groupId?.let {
            return sendGroupMsg(groupId, message, autoEscape, callback)
        }
        throw IllegalStateException("Unknown Exception")
    }

    fun getMsg(messageId: Long, callback: (GetMsg) -> Unit)

    fun deleteMsg(messageId: Long, callback: (NoData) -> Unit)

    fun markMsgAsRead(messageId: Long, callback: (NoData) -> Unit)

    fun getForwardMsg(forwardMessageId: String, callback: (GetForwardMsg) -> Unit)

//    fun sendGroupForwardMsg(groupId: Long,messages:List<MessagePartial>callback:(TODO

    fun getGroupMsgHistory(messageSeq: Long? = null, groupId: Long, callback: (GetGroupMsgHistory) -> Unit)

    fun getImage(file: String, callback: (GetImage) -> Unit)

    fun canSendImage(callback: (CanDo) -> Unit)

//    fun ocrImage(image:String):

    fun canSendRecord(callback: (CanDo) -> Unit)

    fun setFriendAddRequest(flag: String, approve: Boolean = true, remark: String, callback: (NoData) -> Unit)

    fun setGroupAddRequest(
        flag: String,
        subType: String,
        approve: Boolean = true,
        reason: String = "",
        callback: (NoData) -> Unit
    )

    fun getGroupInfo(groupId: Long, noCache: Boolean = false, callback: (GetGroupInfo) -> Unit)

    fun getGroupList(noCache: Boolean = false, callback: (GetGroupList) -> Unit)

    fun getGroupMemberInfo(
        groupId: Long,
        userId: Long,
        noCache: Boolean = false,
        callback: (GetGroupMemberInfo) -> Unit
    )

    fun getGroupMemberList(groupId: Long, noCache: Boolean = false, callback: (GetGroupMemberList) -> Unit)

    fun getGroupHonorInfo(groupId: Long, type: String, callback: (GetGroupHonorInfo) -> Unit)

    //    fun getGroupSystemMsg():GetGroup
    fun getEssenceMsgList(groupId: Long, callback: (GetEssenceMsgList) -> Unit)

    fun getGroupAtAllRemain(groupId: Long, callback: (GetGroupAtAllRemain) -> Unit)

    fun setGroupName(groupId: Long, groupName: String, callback: (NoData) -> Unit)

    fun setGroupPortrait(groupId: Long, file: String, cache: Int = 1, callback: (NoData) -> Unit)

    fun setGroupAdmin(groupId: Long, userId: Long, enable: Boolean = true, callback: (NoData) -> Unit)

    fun setGroupCard(groupId: Long, userId: Long, card: String = "", callback: (NoData) -> Unit)

    fun setGroupSpecialTitle(
        groupId: Long,
        userId: Long,
        specialTitle: String = "",
        duration: Int = -1, callback: (NoData) -> Unit
    )

    fun setGroupBan(groupId: Long, userId: Long, duration: Int = 30 * 60, callback: (NoData) -> Unit)

    fun setGroupWholeBan(groupId: Long, enable: Boolean = true, callback: (NoData) -> Unit)

    fun setGroupAnonymousBan(
        groupId: Long,
        anonymous: Any?,
        anonymousFlag: String?,
        duration: Int,
        callback: (NoData) -> Unit
    )

    fun setEssenceMsg(messageId: Long, callback: (NoData) -> Unit)

    fun sendGroupSign(groupId: Long, callback: (NoData) -> Unit)

    fun sendGroupNotice(groupId: Long, content: String, image: String?, callback: (NoData) -> Unit)

    fun getGroupNotice(groupId: Long, callback: (GetGroupNotice) -> Unit)

    fun setGroupKick(groupId: Long, userId: Long, rejectAddRequest: Boolean = false, callback: (NoData) -> Unit)

    fun setGroupLeave(groupId: Long, isDismiss: Boolean = false, callback: (NoData) -> Unit)

    fun uploadGroupFile(groupId: Long, file: String, name: String, folder: String, callback: (NoData) -> Unit)

    fun deleteGroupFile(groupId: Long, fileId: String, busid: Int, callback: (NoData) -> Unit)

    fun createGroupFileFolder(groupId: Long, name: String, parentId: String = "/", callback: (NoData) -> Unit)

    fun deleteGroupFolder(groupId: Long, folderId: String, callback: (NoData) -> Unit)

    fun getGroupFileSystemInfo(groupId: Long, callback: (GetGroupFileSystemInfo) -> Unit)

    fun getGroupFilesByFolder(groupId: Long, folderId: String, callback: (GetGroupFilesByFolder) -> Unit)

    fun getGroupFileUrl(groupId: Long, fileId: String, busid: Int, callback: (GetGroupFileUrl) -> Unit)

    fun uploadPrivateFile(userId: Long, file: String, name: String, callback: (NoData) -> Unit)

    fun getVersionInfo(callback: (GetVersionInfo) -> Unit)

    fun getStatus(callback: (GetStatus) -> Unit)

    fun reloadEventFilter(file: String, callback: (NoData) -> Unit)

    fun downloadFile(
        url: String,
        threadCount: Int = 4,
        vararg headers: Pair<String, String>,
        callback: (DownloadFile) -> Unit
    )

    fun checkUrlSafely(url: String, callback: (CheckUrlSafely) -> Unit)

//    fun getWordSlices()

    fun handleQuickOperation(context: BasePostEvent, operation: Any, callback: (NoData) -> Unit)
}

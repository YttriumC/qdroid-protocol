package cf.vbnm.amoeba.qdroid.cq.api.enums

import cf.vbnm.amoeba.qdroid.cq.api.BaseApi
import cf.vbnm.amoeba.qdroid.cq.api.data.*
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import java.util.concurrent.atomic.AtomicInteger

@JsonSerialize(using = Action.Serializer::class)
class Action<R, T : BaseApi<R>> private constructor(
    val action: String,
    val respType: Class<T>,
    val returnType: Class<R>
) {
    private val ordinal = Action.ordinal.getAndIncrement()

    companion object {
        private val ordinal = AtomicInteger(0)
        val SEND_PRIVATE_MSG = Action("send_private_msg", MessageIdRet::class.java, MessageIdRet.MessageId::class.java)
        val SEND_GROUP_MSG = Action("send_group_msg", MessageIdRet::class.java, MessageIdRet.MessageId::class.java)

        //        val SEND_GROUP_FORWARD_MSG = Action("send_group_forward_msg", ::class.java)
        val SEND_MSG = Action("send_msg", MessageIdRet::class.java, MessageIdRet.MessageId::class.java)
        val DELETE_MSG = Action("delete_msg", NoData::class.java, Unit::class.java)
        val GET_MSG = Action("get_msg", GetMsg::class.java, GetMsg.MsgDetail::class.java)
        val GET_FORWARD_MSG =
            Action("get_forward_msg", GetForwardMsg::class.java, GetForwardMsg.ForwardMsgs::class.java)
        val GET_IMAGE = Action("get_image", GetImage::class.java, GetImage.ImageDetail::class.java)
        val MARK_MSG_AS_READ = Action("mark_msg_as_read", NoData::class.java, Unit::class.java)
        val SET_GROUP_KICK = Action("set_group_kick", NoData::class.java, Unit::class.java)
        val SET_GROUP_BAN = Action("set_group_ban", NoData::class.java, Unit::class.java)
        val SET_GROUP_ANONYMOUS_BAN = Action("set_group_anonymous_ban", NoData::class.java, Unit::class.java)
        val SET_GROUP_WHOLE_BAN = Action("set_group_whole_ban", NoData::class.java, Unit::class.java)
        val SET_GROUP_ADMIN = Action("set_group_admin", NoData::class.java, Unit::class.java)
        val SET_GROUP_CARD = Action("set_group_card", NoData::class.java, Unit::class.java)
        val SET_GROUP_LEAVE = Action("set_group_leave", NoData::class.java, Unit::class.java)
        val SET_GROUP_SPECIAL_TITLE = Action("set_group_special_title", NoData::class.java, Unit::class.java)
        val SEND_GROUP_SIGN = Action("send_group_sign", NoData::class.java, Unit::class.java)
        val SET_FRIEND_ADD_REQUEST = Action("set_friend_add_request", NoData::class.java, Unit::class.java)
        val SET_GROUP_ADD_REQUEST = Action("set_group_add_request", NoData::class.java, Unit::class.java)
        val GET_LOGIN_INFO = Action("get_login_info", GetLoginInfo::class.java, GetLoginInfo.UserInfo::class.java)
        val GET_QIDIAN_ACCOUNT_INFO =
            Action("qidian_get_account_info", GetQidianInfo::class.java, GetQidianInfo.UserInfo::class.java)
        val SET_QQ_PROFILE = Action("set_qq_profile", NoData::class.java, Unit::class.java)
        val GET_STRANGER_INFO =
            Action("get_stranger_info", GetStrangerInfo::class.java, GetStrangerInfo.StrangerInfo::class.java)
        val GET_FRIEND_LIST =
            Action("get_friend_list", GetFriendList::class.java, GetFriendList.FriendInfoList::class.java)
        val GET_UNIDIRECTIONAL_FRIEND_LIST =
            Action(
                "get_unidirectional_friend_list",
                GetUnidirectionalFriendList::class.java,
                GetUnidirectionalFriendList.UnidirectionalFriendInfo::class.java
            )
        val GET_GROUP_INFO = Action("get_group_info", GetGroupInfo::class.java, GetGroupInfo.GroupInfo::class.java)
        val GET_GROUP_LIST = Action("get_group_list", GetGroupList::class.java, GetGroupList.GroupInfoList::class.java)
        val GET_GROUP_MEMBER_INFO = Action(
            "get_group_member_info",
            GetGroupMemberInfo::class.java,
            GetGroupMemberInfo.GroupMemberInfo::class.java
        )
        val GET_GROUP_MEMBER_LIST = Action(
            "get_group_member_list",
            GetGroupMemberList::class.java,
            GetGroupMemberList.GroupMemberInfoList::class.java
        )
        val GET_GROUP_HONOR_INFO =
            Action("get_group_honor_info", GetGroupHonorInfo::class.java, GetGroupHonorInfo.GroupHonorInfo::class.java)
        val CAN_SEND_IMAGE = Action("can_send_image", CanDo::class.java, CanDo.Yes::class.java)
        val CAN_SEND_RECORD = Action("can_send_record", CanDo::class.java, CanDo.Yes::class.java)
        val GET_VERSION_INFO =
            Action("get_version_info", GetVersionInfo::class.java, GetVersionInfo.VersionDetail::class.java)
        val SET_GROUP_PORTRAIT = Action("set_group_portrait", NoData::class.java, Unit::class.java)

        //        val GET_WORD_SLICES = Action(".get_word_slices", MessageIdRet::class.java)
//        val OCR_IMAGE = Action("ocr_image", MessageIdRet::class.java)
//        val GET_GROUP_SYSTEM_MSG = Action("get_group_system_msg", ::class.java)
        val UPLOAD_PRIVATE_FILE = Action("upload_private_file", NoData::class.java, Unit::class.java)
        val UPLOAD_GROUP_FILE = Action("upload_group_file", NoData::class.java, Unit::class.java)
        val GET_GROUP_FILE_SYSTEM_INFO = Action(
            "get_group_file_system_info",
            GetGroupFileSystemInfo::class.java,
            GetGroupFileSystemInfo.GroupFileSystemInfo::class.java
        )
        val GET_GROUP_ROOT_FILES =
            Action("get_group_root_files", GetGroupRootFiles::class.java, GetGroupRootFiles.GroupFiles::class.java)
        val GET_GROUP_FILES_BY_FOLDER = Action(
            "get_group_files_by_folder",
            GetGroupFilesByFolder::class.java,
            GetGroupFilesByFolder.GroupFiles::class.java
        )
        val CREATE_GROUP_FILE_FOLDER = Action("create_group_file_folder", NoData::class.java, Unit::class.java)
        val DELETE_GROUP_FOLDER = Action("delete_group_folder", NoData::class.java, Unit::class.java)
        val DELETE_GROUP_FILE = Action("delete_group_file", NoData::class.java, Unit::class.java)
        val GET_GROUP_FILE_URL =
            Action("get_group_file_url", GetGroupFileUrl::class.java, GetGroupFileUrl.GroupFileUrl::class.java)
        val GET_STATUS = Action("get_status", GetStatus::class.java, GetStatus.Status::class.java)
        val GET_GROUP_AT_ALL_REMAIN = Action(
            "get_group_at_all_remain",
            GetGroupAtAllRemain::class.java,
            GetGroupAtAllRemain.GroupAtAllRemain::class.java
        )

        //        val HANDLE_QUICK_OPERATION = Action(".handle_quick_operation", ::class.java)
        val SEND_GROUP_NOTICE = Action("_send_group_notice", NoData::class.java, Unit::class.java)
        val GET_GROUP_NOTICE =
            Action("_get_group_notice", GetGroupNotice::class.java, GetGroupNotice.GroupNotice::class.java)
        val RELOAD_EVENT_FILTER = Action("reload_event_filter", NoData::class.java, Unit::class.java)
        val DOWNLOAD_FILE = Action("download_file", DownloadFile::class.java, DownloadFile.DownloadedFile::class.java)
        val GET_ONLINE_CLIENTS =
            Action("get_online_clients", GetOnlineClients::class.java, GetOnlineClients.ClientsList::class.java)
        val GET_GROUP_MSG_HISTORY =
            Action("get_group_msg_history", GetGroupMsgHistory::class.java, GetGroupMsgHistory.Messages::class.java)
        val SET_ESSENCE_MSG = Action("set_essence_msg", NoData::class.java, Unit::class.java)
        val DELETE_ESSENCE_MSG = Action("delete_essence_msg", NoData::class.java, Unit::class.java)
        val GET_ESSENCE_MSG_LIST = Action(
            "get_essence_msg_list",
            GetEssenceMsgList::class.java,
            GetEssenceMsgList.EssenceMsgListList::class.java
        )
        val CHECK_URL_SAFELY =
            Action("check_url_safely", CheckUrlSafely::class.java, CheckUrlSafely.IsUrlSafely::class.java)
        val GET_MODEL_SHOW = Action("_get_model_show", GetModelShow::class.java, GetModelShow.Models::class.java)
        val SET_MODEL_SHOW = Action("_set_model_show", NoData::class.java, Unit::class.java)
        val DELETE_UNIDIRECTIONAL_FRIEND = Action("delete_unidirectional_friend", NoData::class.java, Unit::class.java)
        val SEND_PRIVATE_FORWARD_MSG = Action(
            "send_private_forward_msg",
            SendPrivateForwardMsg::class.java,
            SendPrivateForwardMsg.ForwardMsg::class.java
        )
        val DELETE_FRIEND = Action("delete_friend", NoData::class.java, Unit::class.java)
        val SET_GROUP_NAME = Action("set_group_name", NoData::class.java, Unit::class.java)
        val HANDLE_QUICK_OPERATION = Action("_handle_quick_operation", NoData::class.java, Unit::class.java)
        val SEND_GUILD_CHANNEL_MSG =
            Action("send_guild_channel_msg", StringMessageIdRet::class.java, StringMessageIdRet.MessageId::class.java)
        val GET_GUILD_MSG = Action("get_guild_msg", GetGuildMsg::class.java, GetGuildMsg.GuildMsg::class.java)
    }

    fun cast(baseApi: BaseApi<*>): T {
        @Suppress("UNCHECKED_CAST")
        return baseApi as T
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as Action<*, *>
        return (ordinal == other.ordinal)
    }

    override fun hashCode(): Int {
        return ordinal.hashCode()
    }

    override fun toString(): String {
        return action
    }

    class Serializer : JsonSerializer<Action<*, *>>() {
        override fun serialize(value: Action<*, *>, gen: JsonGenerator, serializers: SerializerProvider?) {
            gen.writeString(value.action)
        }

    }

}
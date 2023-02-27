package cf.vbnm.amoeba.qdroid.cq.api.enums

import cf.vbnm.amoeba.qdroid.cq.api.BaseApi
import cf.vbnm.amoeba.qdroid.cq.api.data.*

class Action<T : BaseApi<*>> private constructor(
    val action: String,
    val returnType: Class<T>,
    private val ordinal: Int
) {
    companion object {
        val SEND_PRIVATE_MSG = Action("send_private_msg", MessageIdRet::class.java, 0)
        val SEND_GROUP_MSG = Action("send_group_msg", MessageIdRet::class.java, 1)

        //        val SEND_GROUP_FORWARD_MSG = Action("send_group_forward_msg", ::class.java, 2)
        val SEND_MSG = Action("send_msg", MessageIdRet::class.java, 3)
        val DELETE_MSG = Action("delete_msg", NoData::class.java, 4)
        val GET_MSG = Action("get_msg", GetMsg::class.java, 5)
        val GET_FORWARD_MSG = Action("get_forward_msg", GetForwardMsg::class.java, 6)
        val GET_IMAGE = Action("get_image", GetImage::class.java, 7)
        val MARK_MSG_AS_READ = Action("mark_msg_as_read", NoData::class.java, 8)
        val SET_GROUP_KICK = Action("set_group_kick", NoData::class.java, 9)
        val SET_GROUP_BAN = Action("set_group_ban", NoData::class.java, 10)
        val SET_GROUP_ANONYMOUS_BAN = Action("set_group_anonymous_ban", NoData::class.java, 11)
        val SET_GROUP_WHOLE_BAN = Action("set_group_whole_ban", NoData::class.java, 12)
        val SET_GROUP_ADMIN = Action("set_group_admin", NoData::class.java, 13)
        val SET_GROUP_CARD = Action("set_group_card", NoData::class.java, 14)
        val SET_GROUP_LEAVE = Action("set_group_leave", NoData::class.java, 15)
        val SET_GROUP_SPECIAL_TITLE = Action("set_group_special_title", NoData::class.java, 16)
        val SEND_GROUP_SIGN = Action("send_group_sign", NoData::class.java, 17)
        val SET_FRIEND_ADD_REQUEST = Action("set_friend_add_request", NoData::class.java, 18)
        val SET_GROUP_ADD_REQUEST = Action("set_group_add_request", NoData::class.java, 19)
        val GET_LOGIN_INFO = Action("get_login_info", GetLoginInfo::class.java, 20)
        val QIDIAN_GET_ACCOUNT_INFO = Action("qidian_get_account_info", GetQidianInfo::class.java, 21)
        val SET_QQ_PROFILE = Action("set_qq_profile", NoData::class.java, 22)
        val GET_STRANGER_INFO = Action("get_stranger_info", GetStrangerInfo::class.java, 23)
        val GET_FRIEND_LIST = Action("get_friend_list", GetFriendList::class.java, 24)
        val GET_UNIDIRECTIONAL_FRIEND_LIST =
            Action("get_unidirectional_friend_list", GetUnidirectionalFriendList::class.java, 25)
        val GET_GROUP_INFO = Action("get_group_info", GetGroupInfo::class.java, 26)
        val GET_GROUP_LIST = Action("get_group_list", GetGroupList::class.java, 27)
        val GET_GROUP_MEMBER_INFO = Action("get_group_member_info", GetGroupMemberInfo::class.java, 28)
        val GET_GROUP_MEMBER_LIST = Action("get_group_member_list", GetGroupMemberList::class.java, 29)
        val GET_GROUP_HONOR_INFO = Action("get_group_honor_info", GetGroupHonorInfo::class.java, 30)
        val CAN_SEND_IMAGE = Action("can_send_image", CanDo::class.java, 31)
        val CAN_SEND_RECORD = Action("can_send_record", CanDo::class.java, 32)
        val GET_VERSION_INFO = Action("get_version_info", GetVersionInfo::class.java, 33)
        val SET_GROUP_PORTRAIT = Action("set_group_portrait", NoData::class.java, 34)

        //        val GET_WORD_SLICES = Action(".get_word_slices", MessageIdRet::class.java, 35)
//        val OCR_IMAGE = Action("ocr_image", MessageIdRet::class.java, 36)
//        val GET_GROUP_SYSTEM_MSG = Action("get_group_system_msg", ::class.java, 37)
        val UPLOAD_PRIVATE_FILE = Action("upload_private_file", NoData::class.java, 38)
        val UPLOAD_GROUP_FILE = Action("upload_group_file", NoData::class.java, 39)
        val GET_GROUP_FILE_SYSTEM_INFO = Action("get_group_file_system_info", GetGroupFileSystemInfo::class.java, 40)
        val GET_GROUP_ROOT_FILES = Action("get_group_root_files", GetGroupRootFiles::class.java, 41)
        val GET_GROUP_FILES_BY_FOLDER = Action("get_group_files_by_folder", GetGroupFilesByFolder::class.java, 42)
        val CREATE_GROUP_FILE_FOLDER = Action("create_group_file_folder", NoData::class.java, 43)
        val DELETE_GROUP_FOLDER = Action("delete_group_folder", NoData::class.java, 44)
        val DELETE_GROUP_FILE = Action("delete_group_file", NoData::class.java, 45)
        val GET_GROUP_FILE_URL = Action("get_group_file_url", GetGroupFileUrl::class.java, 46)
        val GET_STATUS = Action("get_status", GetStatus::class.java, 47)
        val GET_GROUP_AT_ALL_REMAIN = Action("get_group_at_all_remain", GetGroupAtAllRemain::class.java, 62)

        //        val HANDLE_QUICK_OPERATION = Action(".handle_quick_operation", ::class.java, 48)
        val SEND_GROUP_NOTICE = Action("_send_group_notice", NoData::class.java, 49)
        val GET_GROUP_NOTICE = Action("_get_group_notice", GetGroupNotice::class.java, 63)
        val RELOAD_EVENT_FILTER = Action("reload_event_filter", NoData::class.java, 50)
        val DOWNLOAD_FILE = Action("download_file", DownloadFile::class.java, 51)
        val GET_ONLINE_CLIENTS = Action("get_online_clients", GetOnlineClients::class.java, 52)
        val GET_GROUP_MSG_HISTORY = Action("get_group_msg_history", GetGroupMsgHistory::class.java, 53)
        val SET_ESSENCE_MSG = Action("set_essence_msg", NoData::class.java, 54)
        val DELETE_ESSENCE_MSG = Action("delete_essence_msg", NoData::class.java, 55)
        val GET_ESSENCE_MSG_LIST = Action("get_essence_msg_list", GetEssenceMsgList::class.java, 56)
        val CHECK_URL_SAFELY = Action("check_url_safely", CheckUrlSafely::class.java, 57)
        val GET_MODEL_SHOW = Action("_get_model_show", GetModelShow::class.java, 58)
        val SET_MODEL_SHOW = Action("_set_model_show", NoData::class.java, 59)
        val DELETE_UNIDIRECTIONAL_FRIEND = Action("delete_unidirectional_friend", NoData::class.java, 60)
        val SEND_PRIVATE_FORWARD_MSG = Action("send_private_forward_msg", SendPrivateForwardMsg::class.java, 61)
    }

    fun cast(baseApi: BaseApi<*>): T {
        @Suppress("UNCHECKED_CAST")
        return baseApi as T
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as Action<*>
        return (ordinal == other.ordinal)
    }

    override fun hashCode(): Int {
        return ordinal
    }

    override fun toString(): String {
        return action
    }


}
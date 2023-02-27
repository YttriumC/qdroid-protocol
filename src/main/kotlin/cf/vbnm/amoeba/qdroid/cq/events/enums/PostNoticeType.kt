package cf.vbnm.amoeba.qdroid.cq.events.enums

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.annotation.JsonDeserialize

@JsonDeserialize(using = PostNoticeType.Deserializer::class)
enum class PostNoticeType(val type: String, val desc: String) {
    GROUP_UPLOAD("group_upload", "群文件上传"),
    GROUP_ADMIN("group_admin", "群管理员变更"),
    GROUP_DECREASE("group_decrease", "群成员减少"),
    GROUP_INCREASE("group_increase", "群成员增加"),
    GROUP_BAN("group_ban", "群成员禁言"),
    FRIEND_ADD("friend_add", "好友添加"),
    GROUP_RECALL("group_recall", "群消息撤回"),
    FRIEND_RECALL("friend_recall", "好友消息撤回"),
    GROUP_CARD("group_card", "群名片变更"),
    OFFLINE_FILE("offline_file", "离线文件上传"),
    CLIENT_STATUS("client_status", "客户端状态变更"),
    ESSENCE("essence", "精华消息"),
    NOTIFY("notify", "系统通知");

    override fun toString(): String {
        return type
    }

    companion object {
        fun parseType(type: String): PostNoticeType {
            PostNoticeType.values().forEach {
                if (it.type == type)
                    return it
            }
            throw IllegalArgumentException("No such type: $type")
        }
    }

    class Deserializer : JsonDeserializer<PostNoticeType>() {
        override fun deserialize(p: JsonParser, ctxt: DeserializationContext?): PostNoticeType {
            return parseType(p.text)
        }
    }
}
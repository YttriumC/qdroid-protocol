package cf.vbnm.amoeba.qdroid.cq.events.enums

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.annotation.JsonDeserialize

@JsonDeserialize(using = PostMessageSubType.Deserializer::class)
enum class PostMessageSubType(val type: String, val desc: String) {
    FRIEND("friend", "好友"),
    NORMAL("normal", "群聊"),
    ANONYMOUS("anonymous", "匿名"),
    GROUP_SELF("group_self", "群中自身发送"),
    GROUP("group", "群临时会话"),
    NOTICE("notice", "系统提示");

    override fun toString(): String {
        return type
    }

    companion object {
        fun parseType(type: String): PostMessageSubType {
            PostMessageSubType.values().forEach {
                if (it.type == type)
                    return it
            }
            throw IllegalArgumentException("No such type: $type")
        }
    }

    class Deserializer : JsonDeserializer<PostMessageSubType>() {
        override fun deserialize(p: JsonParser, ctxt: DeserializationContext?): PostMessageSubType {
            return parseType(p.text)
        }
    }
}
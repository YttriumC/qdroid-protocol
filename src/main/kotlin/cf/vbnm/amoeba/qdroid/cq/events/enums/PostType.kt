package cf.vbnm.amoeba.qdroid.cq.events.enums

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.annotation.JsonDeserialize

@JsonDeserialize(using = PostType.Deserializer::class)
enum class PostType(val type: String, val desc: String) {
    MESSAGE("message", "消息, 例如, 群聊消息"),
    MESSAGE_SENT("message_sent", "消息发送, 例如, 群聊消息"),
    REQUEST("request", "请求, 例如, 好友申请"),
    NOTICE("notice", "通知, 例如, 群成员增加"),
    META_EVENT("meta_event", "元事件, 例如, go-cqhttp 心跳包");

    override fun toString(): String {
        return type
    }

    companion object {
        fun parseType(type: String): PostType {
            PostType.values().forEach {
                if (it.type == type)
                    return it
            }
            throw IllegalArgumentException("No such type: $type")
        }
    }

    class Deserializer : JsonDeserializer<PostType>() {
        override fun deserialize(p: JsonParser, ctxt: DeserializationContext?): PostType {
            return parseType(p.text)
        }
    }
}


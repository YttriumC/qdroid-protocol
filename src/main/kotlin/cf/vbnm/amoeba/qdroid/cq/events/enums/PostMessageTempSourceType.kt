package cf.vbnm.amoeba.qdroid.cq.events.enums

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.annotation.JsonDeserialize

@JsonDeserialize(using = PostMessageTempSourceType.Deserializer::class)
enum class PostMessageTempSourceType(val type: Int, val desc: String) {
    GROUP(0, "群聊"),
    CONSULTATION(1, "QQ咨询"),
    FIND(2, "查找"),
    MOVIE(3, "QQ电影"),
    TALK(4, "热聊"),
    VERIFICATION(6, "验证消息"),
    DISCUSSION(7, "多人聊天"),
    DATE(8, "约会"),
    CONTACT(9, "通讯录");

    override fun toString(): String {
        return type.toString()
    }

    companion object {
        fun parseType(type: Int): PostMessageTempSourceType {
            PostMessageTempSourceType.values().forEach {
                if (it.type == type)
                    return it
            }
            throw IllegalArgumentException("No such source: $type")
        }

        fun parseType(type: String): PostMessageTempSourceType {
            val intVar = type.toInt()
            PostMessageTempSourceType.values().forEach {
                if (it.type == intVar)
                    return it
            }
            throw IllegalArgumentException("No such source: $type")
        }
    }

    class Deserializer : JsonDeserializer<PostMessageTempSourceType>() {
        override fun deserialize(p: JsonParser, ctxt: DeserializationContext?): PostMessageTempSourceType {
            return parseType(p.text)
        }
    }
}
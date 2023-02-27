package cf.vbnm.amoeba.qdroid.cq.events.enums

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.annotation.JsonDeserialize

@JsonDeserialize(using = PostMessageType.Deserializer::class)
enum class PostMessageType(val type: String, val desc: String) {
    PRIVATE("private", "私聊消息"),
    GROUP("group", "群消息");

    override fun toString(): String {
        return type
    }

    companion object {
        fun parseType(type: String): PostMessageType {
            PostMessageType.values().forEach {
                if (it.type == type)
                    return it
            }
            throw IllegalArgumentException("No such type: $type")
        }
    }

    class Deserializer : JsonDeserializer<PostMessageType>() {
        override fun deserialize(p: JsonParser, ctxt: DeserializationContext?): PostMessageType {
            return parseType(p.text)
        }
    }
}
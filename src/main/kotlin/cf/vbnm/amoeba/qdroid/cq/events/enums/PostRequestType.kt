package cf.vbnm.amoeba.qdroid.cq.events.enums

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.annotation.JsonDeserialize

@JsonDeserialize(using = PostRequestType.Deserializer::class)
enum class PostRequestType(val type: String, val desc: String) {
    FRIEND("friend", "好友请求"),
    GROUP("group", "群请求");

    override fun toString(): String {
        return type
    }

    companion object {
        fun parseType(type: String): PostRequestType {
            PostRequestType.values().forEach {
                if (it.type == type)
                    return it
            }
            throw IllegalArgumentException("No such type: $type")
        }
    }

    class Deserializer : JsonDeserializer<PostRequestType>() {
        override fun deserialize(p: JsonParser, ctxt: DeserializationContext?): PostRequestType {
            return parseType(p.text)
        }
    }
}
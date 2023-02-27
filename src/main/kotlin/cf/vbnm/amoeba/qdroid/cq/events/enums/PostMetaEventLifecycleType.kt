package cf.vbnm.amoeba.qdroid.cq.events.enums

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.annotation.JsonDeserialize

@JsonDeserialize(using = PostMetaEventLifecycleType.Deserializer::class)
enum class PostMetaEventLifecycleType(val type: String, val desc: String) {
    ENABLE("enable", "启用"),
    DISABLE("disable", "禁用"),
    CONNECT("connect", "连接");

    override fun toString(): String {
        return type
    }

    companion object {
        fun parseType(type: String): PostMetaEventLifecycleType {
            PostMetaEventLifecycleType.values().forEach {
                if (it.type == type)
                    return it
            }
            throw IllegalArgumentException("No such type: $type")
        }
    }

    class Deserializer : JsonDeserializer<PostMetaEventLifecycleType>() {
        override fun deserialize(p: JsonParser, ctxt: DeserializationContext?): PostMetaEventLifecycleType {
            return parseType(p.text)
        }
    }
}
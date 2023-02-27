package cf.vbnm.amoeba.qdroid.cq.events.enums

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.annotation.JsonDeserialize

@JsonDeserialize(using = PostMetaEventType.Deserializer::class)
enum class PostMetaEventType(val type: String, val desc: String) {
    LIFECYCLE("lifecycle", "生命周期"),
    HEARTBEAT("heartbeat", "心跳包");

    override fun toString(): String {
        return type
    }

    companion object {
        fun parseType(type: String): PostMetaEventType {
            PostMetaEventType.values().forEach {
                if (it.type == type)
                    return it
            }
            throw IllegalArgumentException("No such type: $type")
        }
    }

    class Deserializer : JsonDeserializer<PostMetaEventType>() {
        override fun deserialize(p: JsonParser, ctxt: DeserializationContext?): PostMetaEventType {
            return parseType(p.text)
        }
    }
}
package cf.vbnm.amoeba.qdroid.cq.events.enums

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.annotation.JsonDeserialize

@JsonDeserialize(using = PostNoticeNotifySubType.Deserializer::class)
enum class PostNoticeNotifySubType(val type: String, val desc: String) {
    HONOR("honor", "群荣誉变更"),
    POKE("poke", "戳一戳"),
    LUCKY_KING("lucky_king", "群红包幸运王"),
    TITLE("title", "群成员头衔变更");

    override fun toString(): String {
        return type
    }

    companion object {
        fun parseType(type: String): PostNoticeNotifySubType {
            PostNoticeNotifySubType.values().forEach {
                if (it.type == type)
                    return it
            }
            throw IllegalArgumentException("No such type: $type")
        }
    }

    class Deserializer : JsonDeserializer<PostNoticeNotifySubType>() {
        override fun deserialize(p: JsonParser, ctxt: DeserializationContext?): PostNoticeNotifySubType {
            return parseType(p.text)
        }
    }
}
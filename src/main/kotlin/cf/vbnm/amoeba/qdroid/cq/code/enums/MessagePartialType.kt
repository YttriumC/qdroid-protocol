package cf.vbnm.amoeba.qdroid.cq.code.enums

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize

/**
 * CQ Code对应的type
 * */
@JsonDeserialize(using = MessagePartialType.Deserializer::class)
@JsonSerialize(using = MessagePartialType.Serializer::class)
@Deprecated("Use MsgPartialType")
enum class MessagePartialType(val type: String, val desc: String) {
    TEXT("text", "文字消息"),
    FACE("face", "QQ 表情"),
    RECORD("record", "语音"),
    VIDEO("video", "短视频"),
    AT("at", "@某人"),
    SHARE("share", "链接分享"),
    MUSIC("music", "音乐分享/音乐自定义分享"),
    IMAGE("image", "图片"),
    REPLY("reply", "回复"),
    RED_BAG("redbag", "红包"),
    POKE("poke", "戳一戳"),
    GIFT("gift", "礼物"),
    FORWARD("forward", "合并转发"),
    NODE("node", "合并转发消息节点"),
    XML("xml", "XML 消息"),
    JSON("json", "JSON 消息"),
    CARD_IMAGE("cardimage", "一种xml的图片消息（装逼大图）"),
    TTS("tts", "文本转语音"), ;

    override fun toString(): String {
        return type
    }

    companion object {
        fun parseType(type: String): MessagePartialType {
            MessagePartialType.values().forEach {
                if (it.type == type)
                    return it
            }
            throw IllegalArgumentException("No such type: $type")
        }
    }

    class Deserializer : JsonDeserializer<MessagePartialType>() {
        override fun deserialize(p: JsonParser, ctxt: DeserializationContext?): MessagePartialType {
            return parseType(p.text)
        }
    }

    class Serializer : JsonSerializer<MessagePartialType>() {
        override fun serialize(value: MessagePartialType, gen: JsonGenerator, serializers: SerializerProvider?) {
            gen.writeString(value.type)
        }

    }
}
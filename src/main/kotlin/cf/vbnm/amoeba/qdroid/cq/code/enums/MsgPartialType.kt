package cf.vbnm.amoeba.qdroid.cq.code.enums

import cf.vbnm.amoeba.qdroid.cq.code.BaseMsgPartial
import cf.vbnm.amoeba.qdroid.cq.code.data.*
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import java.util.concurrent.atomic.AtomicInteger

/**
 * CQ Code对应的type
 * */
@JsonDeserialize(using = MsgPartialType.Deserializer::class)
@JsonSerialize(using = MsgPartialType.Serializer::class)
class MsgPartialType<T : BaseMsgPartial<*>> private constructor(
    val type: String,
    val desc: String
) {
    val ordinal = MsgPartialType.ordinal.getAndIncrement()

    init {
        typeMap[type] = this
    }

    companion object {
        private val ordinal = AtomicInteger(0)
        private val typeMap = HashMap<String, MsgPartialType<*>>()
        val TEXT = MsgPartialType<Text>("text", "文字消息")
        val FACE = MsgPartialType<Face>("face", "QQ 表情")
        val RECORD = MsgPartialType<Record>("record", "语音")
        val VIDEO = MsgPartialType<Video>("video", "短视频")
        val AT = MsgPartialType<At>("at", "@某人")
        val SHARE = MsgPartialType<Share>("share", "链接分享")
        val MUSIC = MsgPartialType<Music>("music", "音乐分享/音乐自定义分享")
        val IMAGE = MsgPartialType<Image>("image", "图片")
        val REPLY = MsgPartialType<Reply>("reply", "回复")
        val RED_BAG = MsgPartialType<RedBag>("redbag", "红包")
        val POKE = MsgPartialType<Poke>("poke", "戳一戳")
        val GIFT = MsgPartialType<Gift>("gift", "礼物")
        val FORWARD = MsgPartialType<Forward>("forward", "合并转发")

        //        val NODE = MsgPartialType<>("node", "合并转发消息节点", Node::class.java)
        val XML = MsgPartialType<Xml>("xml", "XML 消息")
        val JSON = MsgPartialType<Json>("json", "JSON 消息")
        val CARD_IMAGE = MsgPartialType<CardImage>("cardimage", "一种xml的图片消息（装逼大图）")
        val TTS = MsgPartialType<Tts>("tts", "文本转语音");

        fun parseType(type: String): MsgPartialType<*> {
            return typeMap[type] ?: throw IllegalArgumentException("No such type: $type")
        }
    }

    override fun toString(): String {
        return type
    }


    class Deserializer : JsonDeserializer<MsgPartialType<*>>() {
        override fun deserialize(p: JsonParser, ctxt: DeserializationContext?): MsgPartialType<*> {
            return parseType(p.text)
        }
    }

    class Serializer : JsonSerializer<MsgPartialType<*>>() {
        override fun serialize(value: MsgPartialType<*>, gen: JsonGenerator, serializers: SerializerProvider?) {
            gen.writeString(value.type)
        }

    }

    fun toType(baseMsgPartial: BaseMsgPartial<*>): T {
        @Suppress("UNCHECKED_CAST")
        return baseMsgPartial as T
    }

    fun <R> toType(baseMsgPartial: BaseMsgPartial<*>, invoke: (T) -> R): R {
        @Suppress("UNCHECKED_CAST")
        return invoke(baseMsgPartial as T)
    }

    fun toTypeOrNull(baseMsgPartial: BaseMsgPartial<*>?): T? {
        if (baseMsgPartial == null) {
            return null
        }
        @Suppress("UNCHECKED_CAST")
        return baseMsgPartial as T
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MsgPartialType<*>

        return ordinal == other.ordinal
    }

    override fun hashCode(): Int {
        return ordinal.hashCode()
    }
}
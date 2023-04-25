package cf.vbnm.amoeba.qdroid.cq.code

import cf.vbnm.amoeba.qdroid.cq.code.data.*
import cf.vbnm.amoeba.qdroid.cq.code.enums.MsgPartialType
import com.fasterxml.jackson.annotation.*

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes(
    value = [
        JsonSubTypes.Type(At::class, name = "at"),
        JsonSubTypes.Type(CardImage::class, name = "cardimage"),
        JsonSubTypes.Type(Face::class, name = "face"),
        JsonSubTypes.Type(Forward::class, name = "forward"),
        JsonSubTypes.Type(Gift::class, name = "gift"),
        JsonSubTypes.Type(Image::class, name = "image"),
        JsonSubTypes.Type(Json::class, name = "json"),
        JsonSubTypes.Type(Music::class, name = "music"),
        JsonSubTypes.Type(Poke::class, name = "poke"),
        JsonSubTypes.Type(Record::class, name = "record"),
        JsonSubTypes.Type(RedBag::class, name = "redbag"),
        JsonSubTypes.Type(Reply::class, name = "reply"),
        JsonSubTypes.Type(Share::class, name = "share"),
        JsonSubTypes.Type(Text::class, name = "text"),
        JsonSubTypes.Type(Tts::class, name = "tts"),
        JsonSubTypes.Type(Video::class, name = "video"),
        JsonSubTypes.Type(Xml::class, name = "xml"),
    ]
)
abstract class BaseMsgPartial<T>(
    @field:JsonIgnore
    val type: MsgPartialType<*>,
    @field:JsonProperty("data")
    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val data: T
) {
    override fun toString(): String {
        return "${this.javaClass.simpleName}{type=$type, data=$data}"
    }
}
package cf.vbnm.amoeba.qdroid.cq.code.data

import cf.vbnm.amoeba.qdroid.cq.code.BaseMsgPartial
import cf.vbnm.amoeba.qdroid.cq.code.enums.MsgPartialType
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

class CardImage(
    @JsonProperty("data")
    data: CardImageData
) : BaseMsgPartial<CardImage.CardImageData>(
    MsgPartialType.CARD_IMAGE, data
) {
    constructor(
        file: String,
        minWidth: Long? = null,
        minHeight: Long? = null,
        maxWidth: Long? = null,
        maxHeight: Long? = null,
        source: String? = null,
        icon: String? = null,
    ) : this(
        CardImageData(file, minWidth, minHeight, maxWidth, maxHeight, source, icon)
    )

    @JsonInclude(JsonInclude.Include.NON_NULL)
    data class CardImageData(
        @JsonProperty("file") val file: String,
        @JsonProperty("minwidth") val minWidth: Long?,
        @JsonProperty("minheight") val minHeight: Long?,
        @JsonProperty("maxwidth") val maxWidth: Long?,
        @JsonProperty("maxheight") val maxHeight: Long?,
        @JsonProperty("source") val source: String?,
        @JsonProperty("icon") val icon: String?,
    )
}
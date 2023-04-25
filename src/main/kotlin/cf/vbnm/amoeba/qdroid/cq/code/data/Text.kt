package cf.vbnm.amoeba.qdroid.cq.code.data

import cf.vbnm.amoeba.qdroid.cq.code.BaseMsgPartial
import cf.vbnm.amoeba.qdroid.cq.code.enums.MsgPartialType
import com.fasterxml.jackson.annotation.JsonProperty

class Text constructor(
    @JsonProperty("data")
    data: TextData
) : BaseMsgPartial<Text.TextData>(MsgPartialType.TEXT, data) {
    constructor(
        text: String,
    ) : this(TextData(text))

    data class TextData(
        @JsonProperty("text")
        val text: String
    )

}
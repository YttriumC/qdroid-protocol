package cf.vbnm.amoeba.qdroid.cq.code.data

import cf.vbnm.amoeba.qdroid.cq.code.BaseMsgPartial
import cf.vbnm.amoeba.qdroid.cq.code.enums.MsgPartialType
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

class At(
    @JsonProperty("data")
    data: AtData
) : BaseMsgPartial<At.AtData>(MsgPartialType.AT, data) {
    constructor(
        qq: String,
        name: String? = null
    ) : this(AtData(qq, name))

    @JsonInclude(JsonInclude.Include.NON_NULL)
    data class AtData(
        @JsonProperty("qq")
        val qq: String,
        @JsonProperty("name")
        val name: String?
    )
}
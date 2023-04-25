package cf.vbnm.amoeba.qdroid.cq.code.data

import cf.vbnm.amoeba.qdroid.cq.code.BaseMsgPartial
import cf.vbnm.amoeba.qdroid.cq.code.enums.MsgPartialType
import com.fasterxml.jackson.annotation.JsonProperty

class Forward(
    @JsonProperty("data")
    data: ForwardData
) : BaseMsgPartial<Forward.ForwardData>(MsgPartialType.FORWARD, data) {
    constructor(
        id: String,
    ) : this(ForwardData(id))

    data class ForwardData(
        @JsonProperty("id") val id: String
    )
}
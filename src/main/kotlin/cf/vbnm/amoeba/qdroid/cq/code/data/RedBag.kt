package cf.vbnm.amoeba.qdroid.cq.code.data

import cf.vbnm.amoeba.qdroid.cq.code.BaseMsgPartial
import cf.vbnm.amoeba.qdroid.cq.code.enums.MsgPartialType
import com.fasterxml.jackson.annotation.JsonProperty

class RedBag(
    @JsonProperty("data")
    data: RedBagData
) : BaseMsgPartial<RedBag.RedBagData>(MsgPartialType.RED_BAG, data) {
    constructor(
        title: String,
    ) : this(RedBagData(title))

    data class RedBagData(
        @JsonProperty("title") val title: String
    )
}
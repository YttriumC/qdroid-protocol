package cf.vbnm.amoeba.qdroid.cq.code.data

import cf.vbnm.amoeba.qdroid.cq.code.BaseMsgPartial
import cf.vbnm.amoeba.qdroid.cq.code.enums.MsgPartialType
import com.fasterxml.jackson.annotation.JsonProperty

class Gift(
    @JsonProperty("data")
    data: GiftData
) : BaseMsgPartial<Gift.GiftData>(MsgPartialType.GIFT, data) {
    constructor(
        qq: Long,
        id: Int,
    ) : this(GiftData(qq, id))

    data class GiftData(
        @JsonProperty("qq") val qq: Long,
        @JsonProperty("id") val id: Int,
    )
}
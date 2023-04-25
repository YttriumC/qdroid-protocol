package cf.vbnm.amoeba.qdroid.cq.code.data

import cf.vbnm.amoeba.qdroid.cq.code.BaseMsgPartial
import cf.vbnm.amoeba.qdroid.cq.code.enums.MsgPartialType
import com.fasterxml.jackson.annotation.JsonProperty

class Poke(
    @JsonProperty("data")
    data: PokeData
) : BaseMsgPartial<Poke.PokeData>(MsgPartialType.POKE, data) {
    constructor(
        qq: Long,
    ) : this(PokeData(qq))

    data class PokeData(
        @JsonProperty("qq") val id: Long
    )
}
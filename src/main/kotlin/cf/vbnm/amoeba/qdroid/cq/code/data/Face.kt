package cf.vbnm.amoeba.qdroid.cq.code.data

import cf.vbnm.amoeba.qdroid.cq.code.BaseMsgPartial
import cf.vbnm.amoeba.qdroid.cq.code.enums.MsgPartialType
import com.fasterxml.jackson.annotation.JsonProperty

class Face(
    @JsonProperty("data")
    data: FaceData
) : BaseMsgPartial<Face.FaceData>(MsgPartialType.FACE, data) {
    constructor(
        id: Int,
    ) : this(FaceData(id))

    data class FaceData(
        @JsonProperty("id") val id: Int
    )
}
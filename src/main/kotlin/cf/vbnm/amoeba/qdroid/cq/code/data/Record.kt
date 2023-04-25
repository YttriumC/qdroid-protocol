package cf.vbnm.amoeba.qdroid.cq.code.data

import cf.vbnm.amoeba.qdroid.cq.code.BaseMsgPartial
import cf.vbnm.amoeba.qdroid.cq.code.enums.MsgPartialType
import com.fasterxml.jackson.annotation.JsonProperty

class Record(
    @JsonProperty("data")
    data: RecordData
) : BaseMsgPartial<Record.RecordData>(MsgPartialType.RECORD, data) {
    constructor(
        file: String,
    ) : this(RecordData(file))

    data class RecordData(
        @JsonProperty("file")
        val file: String
    )
}
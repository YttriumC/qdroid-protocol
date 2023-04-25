package cf.vbnm.amoeba.qdroid.cq.code.data

import cf.vbnm.amoeba.qdroid.cq.code.BaseMsgPartial
import cf.vbnm.amoeba.qdroid.cq.code.enums.MsgPartialType
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

class Reply(
    @JsonProperty("data")
    data: ReplyData
) : BaseMsgPartial<Reply.ReplyData>(MsgPartialType.REPLY, data) {
    constructor(
        id: Int,
        text: String? = null,
        qq: Long? = null,
        time: Long? = null,
        seq: Long? = null,
    ) : this(ReplyData(id, text, qq, time, seq))

    @JsonInclude(JsonInclude.Include.NON_NULL)
    data class ReplyData(
        @JsonProperty("id") val id: Int,
        @JsonProperty("text") val text: String?,
        @JsonProperty("qq") val qq: Long?,
        @JsonProperty("time") val time: Long?,
        @JsonProperty("seq") val seq: Long?,
    )
}
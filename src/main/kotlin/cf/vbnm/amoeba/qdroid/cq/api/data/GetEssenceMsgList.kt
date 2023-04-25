package cf.vbnm.amoeba.qdroid.cq.api.data

import cf.vbnm.amoeba.qdroid.cq.api.BaseApi
import cf.vbnm.amoeba.qdroid.cq.api.enums.Retcode
import cf.vbnm.amoeba.qdroid.cq.api.enums.Status
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.ObjectMapper

class GetEssenceMsgList(
    @JsonProperty("status")
    status: Status,
    @JsonProperty("retcode")
    retcode: Retcode,
    @JsonProperty("msg")
    msg: String? = null,
    @JsonProperty("wording")
    wording: String? = null,
    @JsonProperty("echo")
    echo: String? = null,
    @JsonProperty("data")
    data: MutableList<EssenceMsgList>
) : BaseApi<MutableList<GetEssenceMsgList.EssenceMsgList>>(status, retcode, msg, wording, echo, data) {

    companion object {
        fun parseApiRet(map: Map<String, Any?>, objectMapper: ObjectMapper): BaseApi<*> {
            return objectMapper.convertValue(map, GetEssenceMsgList::class.java)
        }
    }

    data class EssenceMsgList(
        @JsonProperty("sender_id")
        val senderId: Long,
        @JsonProperty("sender_nick")
        val senderNick: String,
        @JsonProperty("sender_time")
        val senderTime: Long,
        @JsonProperty("operator_id")
        val operatorId: Long,
        @JsonProperty("operator_nick")
        val operatorNick: String,
        @JsonProperty("operator_time")
        val operatorTime: Long,
        @JsonProperty("message_id")
        val messageId: Int,
    )
}
package cf.vbnm.amoeba.qdroid.cq.api.data

import cf.vbnm.amoeba.qdroid.cq.api.BaseApi
import cf.vbnm.amoeba.qdroid.cq.api.enums.Retcode
import cf.vbnm.amoeba.qdroid.cq.api.enums.Status
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.ObjectMapper

class MessageIdRet(
    @JsonProperty("status") status: Status,
    @JsonProperty("retcode") retcode: Retcode,
    @JsonProperty("msg") msg: String? = null,
    @JsonProperty("wording") wording: String? = null,
    @JsonProperty("echo") echo: String? = null,
    @JsonProperty("data") data: MessageId
) : BaseApi<MessageIdRet.MessageId>(status, retcode, msg, wording, echo, data) {

    companion object {
        fun parseApiRet(map: Map<String, Any?>, objectMapper: ObjectMapper): BaseApi<*> {
            return objectMapper.convertValue(map, MessageIdRet::class.java)
        }
    }

    data class MessageId(
        @JsonProperty("message_id") val messageId: Int
    )
}
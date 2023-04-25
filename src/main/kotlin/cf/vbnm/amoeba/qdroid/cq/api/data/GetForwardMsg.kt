package cf.vbnm.amoeba.qdroid.cq.api.data

import cf.vbnm.amoeba.qdroid.cq.MessageDetail
import cf.vbnm.amoeba.qdroid.cq.api.BaseApi
import cf.vbnm.amoeba.qdroid.cq.api.enums.Retcode
import cf.vbnm.amoeba.qdroid.cq.api.enums.Status
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.ObjectMapper

class GetForwardMsg(
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
    @JsonProperty("messages")
    data: ForwardMsgs
) : BaseApi<GetForwardMsg.ForwardMsgs>(status, retcode, msg, wording, echo, data) {

    companion object {
        fun parseApiRet(map: Map<String, Any?>, objectMapper: ObjectMapper): BaseApi<*> {
            return objectMapper.convertValue(map, GetForwardMsg::class.java)
        }
    }

    data class ForwardMsgs(
        @JsonProperty("messages")
        val messages: MutableList<ForwardMsgDetail>
    ) {
        data class ForwardMsgDetail(
            @JsonProperty("content")
            val content: MessageDetail,
            @JsonProperty("group_id")
            val groupId: Long,
            @JsonProperty("sender")
            val sender: Sender,
            @JsonProperty("time")
            val time: Int,
        ) {
            data class Sender(
                @JsonProperty("nickname")
                val nickname: String,
                @JsonProperty("user_id")
                val userId: Long,
            )
        }
    }


}
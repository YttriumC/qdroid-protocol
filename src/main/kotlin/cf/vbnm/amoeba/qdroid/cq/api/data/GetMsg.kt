package cf.vbnm.amoeba.qdroid.cq.api.data

import cf.vbnm.amoeba.qdroid.cq.api.BaseApi
import cf.vbnm.amoeba.qdroid.cq.api.enums.Status
import cf.vbnm.amoeba.qdroid.cq.events.enums.PostMessageType
import cf.vbnm.amoeba.qdroid.cq.events.message.MessagePartial
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.ObjectMapper

class GetMsg(
    @JsonProperty("status")
    status: Status,
    @JsonProperty("retcode")
    retcode: Int,
    @JsonProperty("msg")
    msg: String? = null,
    @JsonProperty("wording")
    wording: String? = null,
    @JsonProperty("echo")
    echo: String? = null,
    @JsonProperty("data")
    data: MsgDetail
) : BaseApi<GetMsg.MsgDetail>(status, retcode, msg, wording, echo, data) {

    companion object {
        fun parseApiRet(map: Map<String, Any?>, objectMapper: ObjectMapper): BaseApi<*> {
            return objectMapper.convertValue(map, GetMsg::class.java)
        }
    }

    data class MsgDetail(
        @JsonProperty("group")
        val group: Boolean,
        @JsonProperty("group_id")
        val groupId: Long?,
        @JsonProperty("message_id")
        val messageId: Int,
        @JsonProperty("real_id")
        val realId: Int,
        @JsonProperty("message_type")
        val messageType: PostMessageType,
        @JsonProperty("sender")
        val sender: Sender,
        @JsonProperty("time")
        val time: Int,
        @JsonProperty("message")
        val message: MutableList<MessagePartial>,
        @JsonProperty("raw_message")
        val rawMessage: String,
    ) {
        data class Sender(
            @JsonProperty("nickname")
            val nickname: String,
            @JsonProperty("user_id")
            val userId: Long,
        )
    }
}
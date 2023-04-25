package cf.vbnm.amoeba.qdroid.cq.api.data

import cf.vbnm.amoeba.core.log.Slf4kt
import cf.vbnm.amoeba.qdroid.bot.QBot
import cf.vbnm.amoeba.qdroid.cq.MessageDetail
import cf.vbnm.amoeba.qdroid.cq.api.BaseApi
import cf.vbnm.amoeba.qdroid.cq.api.enums.Retcode
import cf.vbnm.amoeba.qdroid.cq.api.enums.Status
import cf.vbnm.amoeba.qdroid.cq.events.enums.PostMessageType
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.ObjectMapper

private val log = Slf4kt.getLogger(GetMsg::class.java)

class GetMsg(
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
    data: MsgDetail
) : BaseApi<GetMsg.MsgDetail>(status, retcode, msg, wording, echo, data) {

    companion object {
        fun parseApiRet(map: Map<String, Any?>, objectMapper: ObjectMapper): BaseApi<*> {
            return objectMapper.convertValue(map, GetMsg::class.java)
        }
    }

    suspend fun reply(bot: QBot, message: MessageDetail): MessageIdRet {
        data?.let {
            log.info("Reply to {}: {}", this.data.sender.userId, message)
            return when (data.messageType) {
                PostMessageType.PRIVATE -> {
                    bot.sendPrivateMsg(this.data.sender.userId, message = message)
                }

                PostMessageType.GROUP -> {
                    bot.sendGroupMsg(data.groupId!!, message)
                }
            }
        }
        throw NullPointerException("This api has no data.")
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
        val message: MessageDetail,
        @JsonProperty("raw_message")
        val rawMessage: String?,
    ) {
        data class Sender(
            @JsonProperty("nickname")
            val nickname: String,
            @JsonProperty("user_id")
            val userId: Long,
        )
    }
}
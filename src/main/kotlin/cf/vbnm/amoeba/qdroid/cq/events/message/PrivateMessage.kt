package cf.vbnm.amoeba.qdroid.cq.events.message

import cf.vbnm.amoeba.qdroid.cq.MessageDetail
import cf.vbnm.amoeba.qdroid.cq.events.BasePostEvent
import cf.vbnm.amoeba.qdroid.cq.events.Message
import cf.vbnm.amoeba.qdroid.cq.events.enums.PostMessageSubType
import cf.vbnm.amoeba.qdroid.cq.events.enums.PostMessageTempSourceType
import cf.vbnm.amoeba.qdroid.cq.events.enums.PostMessageType
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.ObjectMapper

class PrivateMessage(
    @JsonProperty("self_id")
    selfId: Long,
    @JsonProperty("time")
    time: Int,
    @JsonProperty("sub_type")
    val subType: PostMessageSubType,
    @JsonProperty("message_id")
    messageId: Int,
    @JsonProperty("user_id")
    userId: Long,
    @JsonProperty("message")
    message: MessageDetail,
    @JsonProperty("raw_message")
    rawMessage: String,
    @JsonProperty("font")
    val font: Int,
    @JsonProperty("sender")
    val sender: PrivateMsgSender,
    @JsonProperty("temp_source")
    val tempSource: PostMessageTempSourceType?,
) : Message(selfId, time, messageId, PostMessageType.PRIVATE, message, rawMessage, userId) {

    override fun toPrivateMessage(): PrivateMessage {
        return this
    }

    override fun toString(): String {
        return "PrivateMessage(" +
                "subType=$subType, " +
                "messageId=$messageId, " +
                "userId=$userId, " +
                "message=$message, " +
                "rawMessage='$rawMessage', " +
                "font=$font, " +
                "sender=$sender, " +
                "tempSource=$tempSource" +
                "," +
                " ${super.toString()}"
    }

    class PrivateMsgSender(
        @JsonProperty("user_id")
        userId: Long,
        @JsonProperty("nickname")
        nickname: String,
        @JsonProperty("sex")
        sex: String,
        @JsonProperty("age")
        age: Int,
        @JsonProperty("group_id")
        val groupId: Long?,
    ) : MessageSender(userId, nickname, sex, age) {
        override fun toString(): String {
            return "PrivateMsgSender(groupId=$groupId, ${super.toString()}"
        }
    }

    companion object {
        fun parseEvent(map: Map<String, Any?>, objectMapper: ObjectMapper): BasePostEvent {
            return objectMapper.convertValue(map, PrivateMessage::class.java)
        }
    }
}

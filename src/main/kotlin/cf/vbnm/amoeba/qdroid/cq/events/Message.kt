package cf.vbnm.amoeba.qdroid.cq.events

import cf.vbnm.amoeba.qdroid.cq.MessageDetail
import cf.vbnm.amoeba.qdroid.cq.events.enums.PostMessageType
import cf.vbnm.amoeba.qdroid.cq.events.enums.PostType
import cf.vbnm.amoeba.qdroid.cq.events.message.GroupMessage
import cf.vbnm.amoeba.qdroid.cq.events.message.PrivateMessage
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.ObjectMapper

abstract class Message(
    @JsonProperty("self_id")
    selfId: Long,
    @JsonProperty("time")
    time: Int,
    @JsonProperty("message_id")
    val messageId: Int,
    @JsonProperty("message_type")
    val messageType: PostMessageType,
    @JsonProperty("message")
    val message: MessageDetail,
    @JsonProperty("raw_message")
    val rawMessage: String,
    @JsonProperty("user_id")
    val userId: Long,
) : BasePostEvent(selfId, time, PostType.MESSAGE) {

    open fun toGroupMessage(): GroupMessage {
        throw TypeCastException("Cannot cast message $messageType to GroupMessage")
    }

    fun isGroupMessage(): Boolean {
        return messageType == PostMessageType.GROUP
    }

    open fun toPrivateMessage(): PrivateMessage {
        throw TypeCastException("Cannot cast message $messageType to PrivateMessage")
    }

    fun isPrivateMessage(): Boolean {
        return messageType == PostMessageType.PRIVATE
    }

    override fun toMessage(): Message {
        return this
    }

    override fun toString(): String {
        return "messageId=$messageId, " +
                "messageType=$messageType, " +
                "message=$message, " +
                "rawMessage='$rawMessage', " +
                "userId=$userId" +
                "," +
                " ${super.toString()}"
    }

    companion object {
        fun parseEvent(map: Map<String, Any?>, objectMapper: ObjectMapper): BasePostEvent {
            return when (PostMessageType.parseType(map["message_type"].toString())) {
                PostMessageType.PRIVATE -> PrivateMessage.parseEvent(map, objectMapper)
                PostMessageType.GROUP -> GroupMessage.parseEvent(map, objectMapper)
            }
        }
    }
}
package cf.vbnm.amoeba.qdroid.cq.events

import cf.vbnm.amoeba.core.log.Slf4kt
import cf.vbnm.amoeba.qdroid.bot.QBot
import cf.vbnm.amoeba.qdroid.cq.MessageDetail
import cf.vbnm.amoeba.qdroid.cq.api.data.MessageIdRet
import cf.vbnm.amoeba.qdroid.cq.events.enums.PostMessageType
import cf.vbnm.amoeba.qdroid.cq.events.enums.PostType
import cf.vbnm.amoeba.qdroid.cq.events.message.GroupMessage
import cf.vbnm.amoeba.qdroid.cq.events.message.PrivateMessage
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.ObjectMapper

private val log = Slf4kt.getLogger(Message::class.java)

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

    suspend fun reply(bot: QBot, message: MessageDetail): MessageIdRet {
        log.info("Reply to {}: {}", this.userId, message)
        return when (messageType) {
            PostMessageType.PRIVATE -> {
                val privateMessage = toPrivateMessage()
                if (message.getReply() == null && privateMessage.tempSource == null) message.addReply(messageId)
                privateMessage.tempSource?.let {
                    bot.sendPrivateMsg(userId, privateMessage.sender.groupId, message)
                } ?: bot.sendPrivateMsg(userId, message = message)
            }

            PostMessageType.GROUP -> {
                val groupMessage = toGroupMessage()
                if (message.getReply() == null) message.addReply(messageId)
                bot.sendGroupMsg(groupMessage.groupId, message)
            }
        }
    }

    open fun toGroupMessage(): GroupMessage {
        throw TypeCastException("Cannot cast message $messageType to GroupMessage")
    }

    open suspend fun <R> ifGroupMessage(invoke: suspend (GroupMessage) -> R): R? {
        if (isGroupMessage()) {
            return invoke(toGroupMessage())
        }
        return null
    }

    open suspend fun <R> ifPrivateMessage(invoke: suspend (PrivateMessage) -> R): R? {
        if (isPrivateMessage()) {
            return invoke(toPrivateMessage())
        }
        return null
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
package cf.vbnm.amoeba.qdroid.cq.events.message

import cf.vbnm.amoeba.qdroid.cq.MessageDetail
import cf.vbnm.amoeba.qdroid.cq.events.BasePostEvent
import cf.vbnm.amoeba.qdroid.cq.events.Message
import cf.vbnm.amoeba.qdroid.cq.events.enums.PostMessageSubType
import cf.vbnm.amoeba.qdroid.cq.events.enums.PostMessageTempSourceType
import cf.vbnm.amoeba.qdroid.cq.events.enums.PostMessageType
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.ObjectMapper

class GroupMessage(
    @JsonProperty("self_id")
    selfId: Long,
    @JsonProperty("time")
    time: Int,
    @JsonProperty("sub_type")
    subType: PostMessageSubType,
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
    val sender: GroupMsgSender,
    @JsonProperty("temp_source")
    val tempSource: PostMessageTempSourceType?,
    @JsonProperty("group_id")
    val groupId: Long,
    @JsonProperty("anonymous")
    val anonymous: Anonymous?,
) : Message(selfId, time, messageId, PostMessageType.GROUP, message, rawMessage, userId, subType) {

    override fun toGroupMessage(): GroupMessage {
        return this
    }

    override fun toString(): String {
        return "GroupMessage(" +
                "subType=$subType, " +
                "messageId=$messageId, " +
                "userId=$userId, " +
                "message=$message, " +
                "rawMessage='$rawMessage', " +
                "font=$font, " +
                "sender=$sender, " +
                "tempSource=$tempSource, " +
                "groupId=$groupId, " +
                "anonymous=$anonymous" +
                "," +
                " ${super.toString()}"
    }

    data class Anonymous(
        @JsonProperty("id")
        val id: Long,
        @JsonProperty("name")
        val name: String,
        @JsonProperty("flag")
        val flag: String,
    ) {
        override fun toString(): String {
            return "Anonymous(id=$id, name='$name', flag='$flag')"
        }
    }

    class GroupMsgSender(
        @JsonProperty("user_id")
        userId: Long,
        @JsonProperty("nickname")
        nickname: String,
        @JsonProperty("sex")
        sex: String,
        @JsonProperty("age")
        age: Int,
        @JsonProperty("card")
        val card: String,
        @JsonProperty("area")
        val area: String,
        @JsonProperty("level")
        val level: String,
        @JsonProperty("role")
        val role: String,
        @JsonProperty("title")
        val title: String,
    ) : MessageSender(userId, nickname, sex, age) {
        override fun toString(): String {
            return "GroupMsgSender(" +
                    "card='$card', " +
                    "area='$area', " +
                    "level='$level', " +
                    "role='$role', " +
                    "title='$title'" +
                    "," +
                    " ${super.toString()}"
        }
    }

    companion object {
        fun parseEvent(map: Map<String, Any?>, objectMapper: ObjectMapper): BasePostEvent {
            return objectMapper.convertValue(map, GroupMessage::class.java)
        }
    }
}

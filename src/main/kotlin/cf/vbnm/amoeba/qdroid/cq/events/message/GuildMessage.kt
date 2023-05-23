package cf.vbnm.amoeba.qdroid.cq.events.message


import cf.vbnm.amoeba.qdroid.cq.MessageDetail
import cf.vbnm.amoeba.qdroid.cq.events.BasePostEvent
import cf.vbnm.amoeba.qdroid.cq.events.Message
import cf.vbnm.amoeba.qdroid.cq.events.enums.PostMessageSubType
import cf.vbnm.amoeba.qdroid.cq.events.enums.PostMessageType
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.ObjectMapper

class GuildMessage(
    @JsonProperty("self_id")
    selfId: Long,
    @JsonProperty("channel_id")
    val channelId: Long,
    @JsonProperty("guild_id")
    val guildId: Long,
    @JsonProperty("message")
    message: MessageDetail,
    @JsonProperty("message_id")
    val guildMessageId: String,
    @JsonProperty("self_tiny_id")
    val selfTinyId: String,
    @JsonProperty("sender")
    val sender: Sender,
    @JsonProperty("sub_type")
    subType: PostMessageSubType,
    @JsonProperty("time")
    time: Int,
    @JsonProperty("user_id")
    userId: Long
) : Message(selfId, time, 0, PostMessageType.GUILD, message, "", userId, subType) {

    data class Sender(
        @JsonProperty("nickname")
        val nickname: String,
        @JsonProperty("tiny_id")
        val tinyId: String,
        @JsonProperty("user_id")
        val userId: Long
    )

    override fun toGuildMessage(): GuildMessage {
        return this
    }

    companion object {
        fun parseEvent(map: Map<String, Any?>, objectMapper: ObjectMapper): BasePostEvent {
            return objectMapper.convertValue(map, GuildMessage::class.java)
        }
    }
}
package cf.vbnm.amoeba.qdroid.cq.api.data


import cf.vbnm.amoeba.qdroid.cq.MessageDetail
import cf.vbnm.amoeba.qdroid.cq.api.BaseApi
import cf.vbnm.amoeba.qdroid.cq.api.enums.Retcode
import cf.vbnm.amoeba.qdroid.cq.api.enums.Status
import com.fasterxml.jackson.annotation.JsonProperty

class GetGuildMsg(
    @JsonProperty("status") status: Status,
    @JsonProperty("retcode") retcode: Retcode,
    @JsonProperty("msg") msg: String? = null,
    @JsonProperty("wording") wording: String? = null,
    @JsonProperty("echo") echo: String? = null,
    @JsonProperty("data") data: GuildMsg
) : BaseApi<GetGuildMsg.GuildMsg>(
    status, retcode, msg, wording, echo, data
) {
    class GuildMsg(
        @JsonProperty("channel_id") val channelId: Long,
        @JsonProperty("guild_id") val guildId: Long,
        @JsonProperty("message") val message: MessageDetail,
        @JsonProperty("message_id") val guildMessageId: String,
        @JsonProperty("message_seq") val messageSeq: Int,
        @JsonProperty("sender") val sender: Sender,
        @JsonProperty("message_source") val messageSource: String,
        @JsonProperty("reactions") val reactions: List<String>?,
        @JsonProperty("time") val time: Int,
        @JsonProperty("user_id") val userId: Long
    ) {

        data class Sender(
            @JsonProperty("nickname") val nickname: String,
            @JsonProperty("tiny_id") val tinyId: String,
            @JsonProperty("user_id") val userId: Long
        )
    }

}

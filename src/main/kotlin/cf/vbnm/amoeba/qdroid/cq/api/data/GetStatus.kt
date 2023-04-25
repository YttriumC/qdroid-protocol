package cf.vbnm.amoeba.qdroid.cq.api.data

import cf.vbnm.amoeba.qdroid.cq.api.BaseApi
import cf.vbnm.amoeba.qdroid.cq.api.enums.Retcode
import cf.vbnm.amoeba.qdroid.cq.api.enums.Status
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.ObjectMapper

class GetStatus(
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
    data: GroupMemberInfo
) : BaseApi<GetStatus.GroupMemberInfo>(status, retcode, msg, wording, echo, data) {

    companion object {
        fun parseApiRet(map: Map<String, Any?>, objectMapper: ObjectMapper): BaseApi<*> {
            return objectMapper.convertValue(map, GetStatus::class.java)
        }
    }

    data class GroupMemberInfo(
        @JsonProperty("app_initialized")
        val appInitialized: Boolean,
        @JsonProperty("app_enabled")
        val appEnabled: Boolean,
        @JsonProperty("plugins_good")
        val pluginsGood: Boolean,
        @JsonProperty("app_good")
        val appGood: Boolean,
        @JsonProperty("online")
        val online: Boolean,
        @JsonProperty("good")
        val good: Boolean,
        @JsonProperty("stat")
        val stat: Statistics,
    ) {
        data class Statistics(
            @JsonProperty("PacketReceived")
            val packetReceived: Long,
            @JsonProperty("PacketSent")
            val packetSent: Long,
            @JsonProperty("PacketLost")
            val packetLost: Long,
            @JsonProperty("MessageReceived")
            val messageReceived: Long,
            @JsonProperty("MessageSent")
            val messageSent: Long,
            @JsonProperty("DisconnectTimes")
            val disconnectTimes: Int,
            @JsonProperty("LostTimes")
            val lostTimes: Int,
            @JsonProperty("LastMessageTime")
            val lastMessageTime: Long,
        )
    }
}
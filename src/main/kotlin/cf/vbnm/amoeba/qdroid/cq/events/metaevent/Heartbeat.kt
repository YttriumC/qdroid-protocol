package cf.vbnm.amoeba.qdroid.cq.events.metaevent

import cf.vbnm.amoeba.qdroid.cq.events.BasePostEvent
import cf.vbnm.amoeba.qdroid.cq.events.MetaEvent
import cf.vbnm.amoeba.qdroid.cq.events.enums.PostMetaEventType
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.ObjectMapper

open class Heartbeat(
    @JsonProperty("self_id")
    selfId: Long,
    @JsonProperty("time")
    time: Int,
    @JsonProperty("status")
    val status: Status
) : MetaEvent(selfId, time, PostMetaEventType.HEARTBEAT) {


    override fun toHeartbeat(): Heartbeat {
        return this
    }

    override fun toString(): String {
        return "Heartbeat(status=$status, ${super.toString()}"
    }

    data class Status(
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
        @JsonProperty("stat")
        val stat: StatusStatistics,
    ) {
        override fun toString(): String {
            return "Status(" +
                    "appInitialized=$appInitialized, " +
                    "appEnabled=$appEnabled, " +
                    "pluginsGood=$pluginsGood, " +
                    "appGood=$appGood, " +
                    "online=$online, " +
                    "stat=$stat" +
                    ")"
        }
    }

    data class StatusStatistics(
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
    ) {
        override fun toString(): String {
            return "StatusStatistics(" +
                    "packetReceived=$packetReceived, " +
                    "packetSent=$packetSent, " +
                    "packetLost=$packetLost, " +
                    "messageReceived=$messageReceived, " +
                    "messageSent=$messageSent, " +
                    "disconnectTimes=$disconnectTimes, " +
                    "lostTimes=$lostTimes, " +
                    "lastMessageTime=$lastMessageTime" +
                    ")"
        }
    }

    companion object {
        fun parseEvent(map: Map<String, Any?>, objectMapper: ObjectMapper): BasePostEvent {
            return Heartbeat(
                (map["self_id"] as Number).toLong(),
                map["time"] as Int,
                objectMapper.convertValue(map["status"], Status::class.java)
            )
        }
    }
}


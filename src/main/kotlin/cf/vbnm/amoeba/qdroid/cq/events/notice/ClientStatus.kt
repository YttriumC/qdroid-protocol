package cf.vbnm.amoeba.qdroid.cq.events.notice

import cf.vbnm.amoeba.qdroid.cq.events.BasePostEvent
import cf.vbnm.amoeba.qdroid.cq.events.Notice
import cf.vbnm.amoeba.qdroid.cq.events.enums.PostNoticeType
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.ObjectMapper

class ClientStatus(
    @JsonProperty("self_id")
    selfId: Long,
    @JsonProperty("time")
    time: Int,
    @JsonProperty("client")
    val clients: MutableList<Device>,
    @JsonProperty("online")
    val online: Boolean,
) : Notice(selfId, time, PostNoticeType.FRIEND_RECALL) {

    override fun toClientStatus(): ClientStatus {
        return this
    }

    override fun toString(): String {
        return "ClientStatus(clients=$clients, online=$online, ${super.toString()}"
    }

    data class Device(
        @JsonProperty("app_id")
        val appId: Long,
        @JsonProperty("device_name")
        val deviceName: String,
        @JsonProperty("device_kind")
        val deviceKind: String,
    ) {
        override fun toString(): String {
            return "Device(appId=$appId, deviceName='$deviceName', deviceKind='$deviceKind')"
        }
    }

    companion object {
        fun parseEvent(map: Map<String, Any?>, objectMapper: ObjectMapper): BasePostEvent {
            return objectMapper.convertValue(map, ClientStatus::class.java)
        }
    }

}

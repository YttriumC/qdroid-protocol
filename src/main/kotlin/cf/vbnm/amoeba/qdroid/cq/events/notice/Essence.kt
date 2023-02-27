package cf.vbnm.amoeba.qdroid.cq.events.notice

import cf.vbnm.amoeba.qdroid.cq.events.BasePostEvent
import cf.vbnm.amoeba.qdroid.cq.events.Notice
import cf.vbnm.amoeba.qdroid.cq.events.enums.PostNoticeType
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.ObjectMapper

class Essence(
    @JsonProperty("self_id")
    selfId: Long,
    @JsonProperty("time")
    time: Int,
    @JsonProperty("group_id")
    val groupId: Long,
    @JsonProperty("sender_id")
    val senderId: Long,
    @JsonProperty("sub_type")
    val subType: String,
    @JsonProperty("operator_id")
    val operatorId: Long,
    @JsonProperty("message_id")
    val messageId: Long,
) : Notice(selfId, time, PostNoticeType.GROUP_ADMIN) {
    override fun toEssence(): Essence {
        return this
    }

    override fun toString(): String {
        return "Essence(groupId=$groupId, senderId=$senderId, subType='$subType', operatorId=$operatorId, messageId=$messageId, ${super.toString()}"
    }

    companion object {
        fun parseEvent(map: Map<String, Any?>, objectMapper: ObjectMapper): BasePostEvent {
            return objectMapper.convertValue(map, Essence::class.java)
        }
    }
}
package cf.vbnm.amoeba.qdroid.cq.events.notice

import cf.vbnm.amoeba.qdroid.cq.events.BasePostEvent
import cf.vbnm.amoeba.qdroid.cq.events.Notice
import cf.vbnm.amoeba.qdroid.cq.events.enums.PostNoticeType
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.ObjectMapper

class GroupRecall(
    @JsonProperty("self_id")
    selfId: Long,
    @JsonProperty("time")
    time: Int,
    @JsonProperty("group_id")
    val groupId: Long,
    @JsonProperty("user_id")
    val userId: Long,
    @JsonProperty("operator_id")
    val operatorId: Long,
    @JsonProperty("message_id")
    val messageId: Int,
) : Notice(selfId, time, PostNoticeType.GROUP_RECALL) {
    override fun toGroupRecall(): GroupRecall {
        return this
    }

    override fun toString(): String {
        return "GroupRecall(groupId=$groupId, userId=$userId, " +
                "operatorId=$operatorId, messageId=$messageId, ${super.toString()}"
    }

    companion object {
        fun parseEvent(map: Map<String, Any?>, objectMapper: ObjectMapper): BasePostEvent {
            return GroupRecall(
                (map["self_id"] as Number).toLong(),
                map["time"] as Int,
                (map["group_id"] as Number).toLong(),
                (map["user_id"] as Number).toLong(),
                (map["operator_id"] as Number).toLong(),
                map["message_id"] as Int,
            )
        }
    }
}
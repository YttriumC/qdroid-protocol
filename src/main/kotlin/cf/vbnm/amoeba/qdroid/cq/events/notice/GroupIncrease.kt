package cf.vbnm.amoeba.qdroid.cq.events.notice

import cf.vbnm.amoeba.qdroid.cq.events.BasePostEvent
import cf.vbnm.amoeba.qdroid.cq.events.Notice
import cf.vbnm.amoeba.qdroid.cq.events.enums.PostNoticeType
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.ObjectMapper

class GroupIncrease(
    @JsonProperty("self_id")
    selfId: Long,
    @JsonProperty("time")
    time: Int,
    @JsonProperty("sub_type")
    val subType: String,
    @JsonProperty("group_id")
    val groupId: Long,
    @JsonProperty("operator_id")
    val operatorId: Long,
    @JsonProperty("user_id")
    val messageId: Long,
) : Notice(selfId, time, PostNoticeType.GROUP_INCREASE) {
    override fun toGroupIncrease(): GroupIncrease {
        return this
    }

    override fun toString(): String {
        return "GroupIncrease(" +
                "subType='$subType', " +
                "groupId=$groupId, " +
                "operatorId=$operatorId, " +
                "messageId=$messageId" +
                "," +
                " ${super.toString()}"
    }

    companion object {
        fun parseEvent(map: Map<String, Any?>, objectMapper: ObjectMapper): BasePostEvent {
            return GroupIncrease(
                (map["self_id"] as Number).toLong(),
                map["time"] as Int,
                map["sub_type"] as String,
                (map["group_id"] as Number).toLong(),
                (map["operator_id"] as Number).toLong(),
                (map["user_id"] as Number).toLong(),
            )
        }
    }
}
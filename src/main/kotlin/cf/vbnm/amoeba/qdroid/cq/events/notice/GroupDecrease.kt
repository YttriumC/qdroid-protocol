package cf.vbnm.amoeba.qdroid.cq.events.notice

import cf.vbnm.amoeba.qdroid.cq.events.BasePostEvent
import cf.vbnm.amoeba.qdroid.cq.events.Notice
import cf.vbnm.amoeba.qdroid.cq.events.enums.PostNoticeType
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.ObjectMapper

class GroupDecrease(
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
    val userId: Long,
) : Notice(selfId, time, PostNoticeType.GROUP_DECREASE) {
    override fun toGroupDecrease(): GroupDecrease {
        return this
    }

    override fun toString(): String {
        return "GroupDecrease(" +
                "subType='$subType', " +
                "groupId=$groupId, " +
                "userId=$userId, " +
                "operatorId=$operatorId" +
                "," +
                " ${super.toString()}"
    }

    companion object {
        fun parseEvent(map: Map<String, Any?>, objectMapper: ObjectMapper): BasePostEvent {
            return objectMapper.convertValue(map, GroupDecrease::class.java)
        }
    }
}
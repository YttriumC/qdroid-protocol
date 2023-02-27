package cf.vbnm.amoeba.qdroid.cq.events.notice

import cf.vbnm.amoeba.qdroid.cq.events.BasePostEvent
import cf.vbnm.amoeba.qdroid.cq.events.Notice
import cf.vbnm.amoeba.qdroid.cq.events.enums.PostNoticeType
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.ObjectMapper

class GroupBan(
    @JsonProperty("self_id") selfId: Long,
    @JsonProperty("time") time: Int,
    @JsonProperty("sub_type") val subType: String,
    @JsonProperty("group_id") val groupId: Long,
    @JsonProperty("operator_id") val operatorId: Long,
    @JsonProperty("user_id") val messageId: Long,
    @JsonProperty("duration") val duration: Long,
) : Notice(selfId, time, PostNoticeType.GROUP_BAN) {
    override fun toGroupBan(): GroupBan {
        return this
    }

    override fun toString(): String {
        return "GroupBan(" + "subType='$subType', " + "groupId=$groupId, " +
                "operatorId=$operatorId, " + "messageId=$messageId, " +
                "duration=$duration" + "," + " ${super.toString()}"
    }

    companion object {
        fun parseEvent(map: Map<String, Any?>, objectMapper: ObjectMapper): BasePostEvent {
            return GroupBan(
                (map["self_id"] as Number).toLong(),
                map["time"] as Int,
                map["sub_type"] as String,
                (map["group_id"] as Number).toLong(),
                (map["operator_id"] as Number).toLong(),
                (map["user_id"] as Number).toLong(),
                (map["duration"] as Number).toLong(),
            )
        }
    }
}
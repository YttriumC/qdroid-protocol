package cf.vbnm.amoeba.qdroid.cq.events.notice

import cf.vbnm.amoeba.qdroid.cq.events.BasePostEvent
import cf.vbnm.amoeba.qdroid.cq.events.Notice
import cf.vbnm.amoeba.qdroid.cq.events.enums.PostNoticeType
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.ObjectMapper

class GroupAdmin(
    @JsonProperty("self_id")
    selfId: Long,
    @JsonProperty("time")
    time: Int,
    @JsonProperty("group_id")
    val groupId: Long,
    @JsonProperty("user_id")
    val userId: Long,
    @JsonProperty("sub_type")
    val subType: String,
) : Notice(selfId, time, PostNoticeType.GROUP_ADMIN) {

    override fun toGroupAdmin(): GroupAdmin {
        return this
    }

    override fun toString(): String {
        return "GroupAdmin(groupId=$groupId, userId=$userId, subType='$subType', ${super.toString()}"
    }

    companion object {
        fun parseEvent(map: Map<String, Any?>, objectMapper: ObjectMapper): BasePostEvent {
            return GroupAdmin(
                (map["self_id"] as Number).toLong(),
                map["time"] as Int,
                (map["group_id"] as Number).toLong(),
                (map["user_id"] as Number).toLong(),
                map["sub_type"] as String
            )
        }
    }
}
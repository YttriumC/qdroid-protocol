package cf.vbnm.amoeba.qdroid.cq.events.notice

import cf.vbnm.amoeba.qdroid.cq.events.BasePostEvent
import cf.vbnm.amoeba.qdroid.cq.events.Notice
import cf.vbnm.amoeba.qdroid.cq.events.enums.PostNoticeNotifySubType
import cf.vbnm.amoeba.qdroid.cq.events.enums.PostNoticeType
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.ObjectMapper

class Notify(
    @JsonProperty("self_id")
    selfId: Long,
    @JsonProperty("time")
    time: Int,
    @JsonProperty("sub_type")
    val subType: PostNoticeNotifySubType,
    @JsonProperty("sender_id")
    val senderId: Long?,
    @JsonProperty("group_id")
    val groupId: Long?,
    @JsonProperty("user_id")
    val userId: Long,
    @JsonProperty("target_id")
    val targetId: Long,
    @JsonProperty("honor_type")
    val honorType: String?,
    @JsonProperty("title")
    val title: String?,
) : Notice(selfId, time, PostNoticeType.NOTIFY) {

    override fun toNotify(): Notify {
        return this
    }

    override fun toString(): String {
        return "Notify(" +
                "subType=$subType, " +
                "senderId=$senderId, " +
                "groupId=$groupId, " +
                "userId=$userId, " +
                "targetId=$targetId, " +
                "honorType=$honorType, " +
                "title=$title" +
                "," +
                " ${super.toString()}"
    }

    companion object {
        fun parseEvent(map: Map<String, Any?>, objectMapper: ObjectMapper): BasePostEvent {
            return objectMapper.convertValue(map, Notify::class.java)
        }
    }
}
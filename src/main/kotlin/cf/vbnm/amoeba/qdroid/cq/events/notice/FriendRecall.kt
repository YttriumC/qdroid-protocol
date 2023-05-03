package cf.vbnm.amoeba.qdroid.cq.events.notice

import cf.vbnm.amoeba.qdroid.cq.events.BasePostEvent
import cf.vbnm.amoeba.qdroid.cq.events.Notice
import cf.vbnm.amoeba.qdroid.cq.events.enums.PostNoticeType
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.ObjectMapper

class FriendRecall(
    @JsonProperty("self_id")
    selfId: Long,
    @JsonProperty("time")
    time: Int,
    @JsonProperty("user_id")
    val userId: Long,
    @JsonProperty("message_id")
    val messageId: Long,
) : Notice(selfId, time, PostNoticeType.FRIEND_RECALL) {
    override fun toFriendRecall(): FriendRecall {
        return this
    }

    override fun toString(): String {
        return "FriendRecall(userId=$userId, messageId=$messageId, ${super.toString()}"
    }

    companion object {
        fun parseEvent(map: Map<String, Any?>, objectMapper: ObjectMapper): BasePostEvent {
            return objectMapper.convertValue(map, FriendRecall::class.java)
        }
    }
}
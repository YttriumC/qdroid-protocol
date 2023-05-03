package cf.vbnm.amoeba.qdroid.cq.events.request

import cf.vbnm.amoeba.qdroid.cq.events.BasePostEvent
import cf.vbnm.amoeba.qdroid.cq.events.Request
import cf.vbnm.amoeba.qdroid.cq.events.enums.PostRequestType
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.ObjectMapper

class FriendRequest(
    @JsonProperty("self_id")
    selfId: Long,
    @JsonProperty("time")
    time: Int,
    @JsonProperty("user_id")
    val userId: Long,
    @JsonProperty("comment")
    val comment: String,
    @JsonProperty("flag")
    val flag: String,
) : Request(selfId, time, PostRequestType.FRIEND) {
    override fun toFriendRequest(): FriendRequest {
        return this
    }

    override fun toString(): String {
        return "FriendRequest(userId=$userId, comment='$comment', flag='$flag', ${super.toString()}"
    }

    companion object {
        fun parseEvent(map: Map<String, Any?>, objectMapper: ObjectMapper): BasePostEvent {
            return objectMapper.convertValue(map, FriendRequest::class.java)
        }
    }
}
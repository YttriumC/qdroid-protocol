package cf.vbnm.amoeba.qdroid.cq.events

import cf.vbnm.amoeba.qdroid.cq.events.enums.PostRequestType
import cf.vbnm.amoeba.qdroid.cq.events.enums.PostType
import cf.vbnm.amoeba.qdroid.cq.events.request.FriendRequest
import cf.vbnm.amoeba.qdroid.cq.events.request.GroupRequest
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.ObjectMapper

abstract class Request(
    @JsonProperty("self_id")
    selfId: Long,
    @JsonProperty("time")
    time: Int,
    @JsonProperty("request_type")
    val requestType: PostRequestType
) : BasePostEvent(selfId, time, PostType.REQUEST) {

    open fun toFriendRequest(): FriendRequest {
        throw TypeCastException("Cannot cast request $requestType to FriendRequest")
    }

    fun isFriendRequest(): Boolean {
        return requestType == PostRequestType.FRIEND
    }

    open fun toGroupRequest(): GroupRequest {
        throw TypeCastException("Cannot cast request $requestType to GroupRequest")
    }

    fun isGroupRequest(): Boolean {
        return requestType == PostRequestType.GROUP
    }


    override fun toString(): String {
        return "requestType=$requestType, ${super.toString()}"
    }

    companion object {
        fun parseEvent(map: Map<String, Any?>, objectMapper: ObjectMapper): BasePostEvent {
            return when (PostRequestType.parseType(map["request_type"].toString())) {
                PostRequestType.FRIEND -> FriendRequest.parseEvent(map, objectMapper)
                PostRequestType.GROUP -> GroupRequest.parseEvent(map, objectMapper)
            }
        }
    }
}
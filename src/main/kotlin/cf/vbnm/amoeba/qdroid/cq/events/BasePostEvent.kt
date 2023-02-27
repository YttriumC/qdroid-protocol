package cf.vbnm.amoeba.qdroid.cq.events

import cf.vbnm.amoeba.qdroid.cq.events.enums.PostType
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue

abstract class BasePostEvent(
    @JsonProperty("self_id")
    val selfId: Long,
    @JsonProperty("time")
    val time: Int,
    @JsonProperty("post_type")
    val postType: PostType,
) {

    fun isMetaEvent(): Boolean {
        return postType == PostType.META_EVENT
    }

    fun isNotice(): Boolean {
        return postType == PostType.NOTICE
    }

    fun isRequest(): Boolean {
        return postType == PostType.REQUEST
    }

    fun isMessage(): Boolean {
        return postType == PostType.MESSAGE
    }

    open fun toMetaEvent(): MetaEvent {
        throw TypeCastException("Cannot cast ${postType.name} to MetaEvent")
    }

    open fun toNotice(): Notice {
        throw TypeCastException("Cannot cast ${postType.name} to Notice")
    }

    open fun toRequest(): Request {
        throw TypeCastException("Cannot cast ${postType.name} to Request")
    }

    open fun toMessage(): Message {
        throw TypeCastException("Cannot cast ${postType.name} to Message")
    }

    override fun toString(): String {
        return "selfId=$selfId, time=$time, postType=$postType)"
    }

    companion object {

        fun isPostEvent(map: Map<String, Any?>): Boolean {
            return map["post_type"] != null
        }

        fun isPostEvent(message: String, objectMapper: ObjectMapper): Boolean {
            val map = objectMapper.readValue<Map<String, Any?>>(message)
            return isPostEvent(map)
        }

        fun parseEvent(map: Map<String, Any?>, objectMapper: ObjectMapper): BasePostEvent {
            try {
                return when (PostType.parseType(map["post_type"].toString())) {
                    PostType.MESSAGE, PostType.MESSAGE_SENT -> Message.parseEvent(map, objectMapper)
                    PostType.REQUEST -> Request.parseEvent(map, objectMapper)
                    PostType.NOTICE -> Notice.parseEvent(map, objectMapper)
                    PostType.META_EVENT -> MetaEvent.parseEvent(map, objectMapper)
                }
            } catch (e: Exception) {
                throw IllegalArgumentException("Cannot convert this content to PostEvent: $map", e)
            }
        }
    }
}
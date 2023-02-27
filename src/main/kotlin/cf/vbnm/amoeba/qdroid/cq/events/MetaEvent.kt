package cf.vbnm.amoeba.qdroid.cq.events

import cf.vbnm.amoeba.qdroid.cq.events.enums.PostMetaEventType
import cf.vbnm.amoeba.qdroid.cq.events.enums.PostType
import cf.vbnm.amoeba.qdroid.cq.events.metaevent.Heartbeat
import cf.vbnm.amoeba.qdroid.cq.events.metaevent.Lifecycle
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.ObjectMapper

abstract class MetaEvent(
    @JsonProperty("self_id")
    selfId: Long,
    @JsonProperty("time")
    time: Int,
    @JsonProperty("meta_event_type")
    val metaEventType: PostMetaEventType
) : BasePostEvent(selfId, time, PostType.META_EVENT) {


    override fun toMetaEvent(): MetaEvent {
        return this
    }

    open fun toHeartbeat(): Heartbeat {
        throw TypeCastException("Cannot cast meta event $metaEventType to Heartbeat")
    }

    open fun toLifecycle(): Lifecycle {
        throw TypeCastException("Cannot cast meta event $metaEventType to Lifecycle")
    }

    fun isHeartbeat(): Boolean {
        return metaEventType == PostMetaEventType.HEARTBEAT
    }

    fun isLifecycle(): Boolean {
        return metaEventType == PostMetaEventType.LIFECYCLE
    }

    override fun toString(): String {
        return "metaEventType=$metaEventType, ${super.toString()})"
    }


    companion object {
        fun parseEvent(map: Map<String, Any?>, objectMapper: ObjectMapper): BasePostEvent {
            map["meta_event_type"]?.let {
                return when (PostMetaEventType.parseType(it.toString())) {
                    PostMetaEventType.LIFECYCLE -> Lifecycle.parseEvent(map, objectMapper)
                    PostMetaEventType.HEARTBEAT -> Heartbeat.parseEvent(map, objectMapper)
                }
            }
            throw IllegalArgumentException("Cannot convert this content to MetaEvent: $map")
        }
    }

}
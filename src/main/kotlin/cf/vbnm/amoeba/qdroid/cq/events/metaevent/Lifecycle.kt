package cf.vbnm.amoeba.qdroid.cq.events.metaevent

import cf.vbnm.amoeba.qdroid.cq.events.BasePostEvent
import cf.vbnm.amoeba.qdroid.cq.events.MetaEvent
import cf.vbnm.amoeba.qdroid.cq.events.enums.PostMetaEventLifecycleType
import cf.vbnm.amoeba.qdroid.cq.events.enums.PostMetaEventType
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.ObjectMapper

open class Lifecycle(
    @JsonProperty("self_id")
    selfId: Long,
    @JsonProperty("time")
    time: Int,
    @JsonProperty("sub_type")
    val lifecycleType: PostMetaEventLifecycleType
) : MetaEvent(selfId, time, PostMetaEventType.LIFECYCLE) {

    override fun toLifecycle(): Lifecycle {
        return this
    }

    override fun toString(): String {
        return "Lifecycle(lifecycleType=$lifecycleType, ${super.toString()}"
    }

    companion object {
        fun parseEvent(map: Map<String, Any?>, objectMapper: ObjectMapper): BasePostEvent {
            return Lifecycle(
                (map["self_id"] as Number).toLong(),
                map["time"] as Int,
                PostMetaEventLifecycleType.parseType(map["sub_type"].toString())
            )
        }
    }

}
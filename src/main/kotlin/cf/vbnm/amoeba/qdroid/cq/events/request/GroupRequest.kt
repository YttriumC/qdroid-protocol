package cf.vbnm.amoeba.qdroid.cq.events.request

import cf.vbnm.amoeba.qdroid.cq.events.BasePostEvent
import cf.vbnm.amoeba.qdroid.cq.events.Request
import cf.vbnm.amoeba.qdroid.cq.events.enums.PostRequestType
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.ObjectMapper

class GroupRequest(
    @JsonProperty("self_id")
    selfId: Long,
    @JsonProperty("time")
    time: Int,
    @JsonProperty("sub_type")
    val subType: String,
    @JsonProperty("group_id")
    val groupId: Long,
    @JsonProperty("user_id")
    val userId: Long,
    @JsonProperty("comment")
    val comment: String,
    @JsonProperty("flag")
    val flag: String,
) : Request(selfId, time, PostRequestType.GROUP) {
    override fun toGroupRequest(): GroupRequest {
        return this
    }

    override fun toString(): String {
        return "GroupRequest(" +
                "subType='$subType', " +
                "groupId=$groupId, " +
                "userId=$userId, " +
                "comment='$comment', " +
                "flag='$flag'" +
                "," +
                " ${super.toString()}"
    }

    companion object {
        fun parseEvent(map: Map<String, Any?>, objectMapper: ObjectMapper): BasePostEvent {
            return GroupRequest(
                map["self_id"].let {
                    if (it is String) {
                        return@let it.toLong()
                    }
                    it as Long
                },
                map["time"] as Int,
                map["sub_type"] as String,
                (map["group_id"] as Number).toLong(),
                (map["user_id"] as Number).toLong(),
                map["comment"] as String,
                map["flag"] as String
            )
            /*GroupRequest::class.constructors.first().let { kFunction ->
                val arr = arrayOfNulls<Any>(kFunction.parameters.size)
                val params = ArrayList<Any>()
                kFunction.parameters.forEach {//方法参数
                    val name = it.findAnnotations(JsonProperty::class).first().value
                    val kClass = it.type.isSubtypeOf(KType)
                    kClass.isFinal
                }
                return kFunction.call(*arr)
            }*/
        }
    }
}
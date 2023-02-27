package cf.vbnm.amoeba.qdroid.cq.events.notice

import cf.vbnm.amoeba.qdroid.cq.events.BasePostEvent
import cf.vbnm.amoeba.qdroid.cq.events.Notice
import cf.vbnm.amoeba.qdroid.cq.events.enums.PostNoticeType
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.ObjectMapper

class GroupUpload(
    @JsonProperty("self_id")
    selfId: Long,
    @JsonProperty("time")
    time: Int,
    @JsonProperty("group_id")
    val groupId: Long,
    @JsonProperty("user_id")
    val userId: Long,
    @JsonProperty("file")
    val file: GroupFile,
) : Notice(selfId, time, PostNoticeType.GROUP_UPLOAD) {
    override fun toGroupUpload(): GroupUpload {
        return this
    }

    override fun toString(): String {
        return "GroupUpload(groupId=$groupId, userId=$userId, file=$file, ${super.toString()}"
    }

    data class GroupFile(
        @JsonProperty("id")
        val id: String,
        @JsonProperty("name")
        val name: String,
        @JsonProperty("size")
        val size: Long,
        @JsonProperty("busid")
        val busid: Long,
    ) {
        override fun toString(): String {
            return "GroupFile(id='$id', name='$name', size=$size, busid=$busid)"
        }
    }

    companion object {
        fun parseEvent(map: Map<String, Any?>, objectMapper: ObjectMapper): BasePostEvent {
            return GroupUpload(
                (map["self_id"] as Number).toLong(),
                map["time"] as Int,
                (map["group_id"] as Number).toLong(),
                (map["user_id"] as Number).toLong(),
                objectMapper.convertValue(map["file"], GroupFile::class.java),
            )
        }
    }

}

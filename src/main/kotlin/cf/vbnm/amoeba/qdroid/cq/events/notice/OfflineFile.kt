package cf.vbnm.amoeba.qdroid.cq.events.notice

import cf.vbnm.amoeba.qdroid.cq.events.BasePostEvent
import cf.vbnm.amoeba.qdroid.cq.events.Notice
import cf.vbnm.amoeba.qdroid.cq.events.enums.PostNoticeType
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.ObjectMapper

class OfflineFile(
    @JsonProperty("self_id")
    selfId: Long,
    @JsonProperty("time")
    time: Int,
    @JsonProperty("user_id")
    val userId: Long,
    @JsonProperty("file")
    val file: PersonalFile,
) : Notice(selfId, time, PostNoticeType.OFFLINE_FILE) {
    override fun toOfflineFile(): OfflineFile {
        return this
    }

    override fun toString(): String {
        return "OfflineFile(userId=$userId, file=$file, ${super.toString()}"
    }

    data class PersonalFile(
        @JsonProperty("name")
        val name: String,
        @JsonProperty("size")
        val size: Long,
        @JsonProperty("url")
        val url: String,
    ) {
        override fun toString(): String {
            return "PersonalFile(name='$name', size=$size, url='$url')"
        }
    }

    companion object {
        fun parseEvent(map: Map<String, Any?>, objectMapper: ObjectMapper): BasePostEvent {
            return objectMapper.convertValue(map, OfflineFile::class.java)
        }
    }
}

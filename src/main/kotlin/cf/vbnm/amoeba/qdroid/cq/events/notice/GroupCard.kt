package cf.vbnm.amoeba.qdroid.cq.events.notice

import cf.vbnm.amoeba.qdroid.cq.events.BasePostEvent
import cf.vbnm.amoeba.qdroid.cq.events.Notice
import cf.vbnm.amoeba.qdroid.cq.events.enums.PostNoticeType
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.ObjectMapper

class GroupCard(
    @JsonProperty("self_id")
    selfId: Long,
    @JsonProperty("time")
    time: Int,
    @JsonProperty("group_id")
    val groupId: Long,
    @JsonProperty("user_id")
    val userId: Long,
    @JsonProperty("card_new")
    val cardNew: String?,
    @JsonProperty("card_old")
    val cardOld: String?,
) : Notice(selfId, time, PostNoticeType.GROUP_CARD) {
    override fun toGroupCard(): GroupCard {
        return this
    }

    override fun toString(): String {
        return "GroupCard(groupId=$groupId, userId=$userId, cardNew=$cardNew, cardOld=$cardOld, ${super.toString()}"
    }

    companion object {
        fun parseEvent(map: Map<String, Any?>, objectMapper: ObjectMapper): BasePostEvent {
            return objectMapper.convertValue(map, GroupCard::class.java)
        }
    }
}
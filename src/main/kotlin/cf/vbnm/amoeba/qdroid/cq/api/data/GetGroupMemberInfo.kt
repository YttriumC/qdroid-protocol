package cf.vbnm.amoeba.qdroid.cq.api.data

import cf.vbnm.amoeba.qdroid.cq.api.BaseApi
import cf.vbnm.amoeba.qdroid.cq.api.enums.Retcode
import cf.vbnm.amoeba.qdroid.cq.api.enums.Status
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.ObjectMapper

class GetGroupMemberInfo(
    @JsonProperty("status")
    status: Status,
    @JsonProperty("retcode")
    retcode: Retcode,
    @JsonProperty("msg")
    msg: String? = null,
    @JsonProperty("wording")
    wording: String? = null,
    @JsonProperty("echo")
    echo: String? = null,
    @JsonProperty("data")
    data: GroupMemberInfo
) : BaseApi<GetGroupMemberInfo.GroupMemberInfo>(status, retcode, msg, wording, echo, data) {

    companion object {
        fun parseApiRet(map: Map<String, Any?>, objectMapper: ObjectMapper): BaseApi<*> {
            return objectMapper.convertValue(map, GetGroupMemberInfo::class.java)
        }
    }

    data class GroupMemberInfo(
        @JsonProperty("group_id")
        val groupId: Long,
        @JsonProperty("user_id")
        val userId: Long,
        @JsonProperty("nickname")
        val nickname: String,
        @JsonProperty("card")
        val card: String,
        @JsonProperty("sex")
        val sex: String,
        @JsonProperty("age")
        val age: Int,
        @JsonProperty("area")
        val area: String,
        @JsonProperty("join_time")
        val joinTime: Int,
        @JsonProperty("last_sent_time")
        val lastSentTime: Int,
        @JsonProperty("level")
        val level: String,
        @JsonProperty("role")
        val role: String,
        @JsonProperty("unfriendly")
        val unfriendly: String,
        @JsonProperty("title")
        val title: String,
        @JsonProperty("title_expire_time")
        val titleExpireTime: Long,
        @JsonProperty("card_changeable")
        val cardChangeable: Boolean,
        @JsonProperty("shut_up_timestamp")
        val shutUpTimestamp: Long,
    )
}
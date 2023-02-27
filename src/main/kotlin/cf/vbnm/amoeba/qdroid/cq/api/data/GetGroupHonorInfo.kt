package cf.vbnm.amoeba.qdroid.cq.api.data

import cf.vbnm.amoeba.qdroid.cq.api.BaseApi
import cf.vbnm.amoeba.qdroid.cq.api.enums.Status
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.ObjectMapper

class GetGroupHonorInfo(
    @JsonProperty("status")
    status: Status,
    @JsonProperty("retcode")
    retcode: Int,
    @JsonProperty("msg")
    msg: String? = null,
    @JsonProperty("wording")
    wording: String? = null,
    @JsonProperty("echo")
    echo: String? = null,
    @JsonProperty("data")
    data: GroupHonorInfo
) : BaseApi<GetGroupHonorInfo.GroupHonorInfo>(status, retcode, msg, wording, echo, data) {

    companion object {
        fun parseApiRet(map: Map<String, Any?>, objectMapper: ObjectMapper): BaseApi<*> {
            return objectMapper.convertValue(map, GetGroupHonorInfo::class.java)
        }
    }

    data class GroupHonorInfo(
        @JsonProperty("group_id")
        val groupId: Long,
        @JsonProperty("current_talkative")
        val currentTalkative: CurrentTalkative?,
        @JsonProperty("talkative_list")
        val talkativeList: MutableList<OtherHonor>?,
        @JsonProperty("performer_list")
        val performerList: MutableList<OtherHonor>?,
        @JsonProperty("legend_list")
        val legendList: MutableList<OtherHonor>?,
        @JsonProperty("strong_newbie_list")
        val strongNewbieList: MutableList<OtherHonor>?,
        @JsonProperty("emotion_list")
        val emotionList: MutableList<OtherHonor>?,
    ) {
        data class CurrentTalkative(
            @JsonProperty("group_id")
            val groupId: Long,
            @JsonProperty("nickname")
            val nickname: String,
            @JsonProperty("avatar")
            val avatar: String,
            @JsonProperty("day_count")
            val dayCount: Int,
        )

        data class OtherHonor(
            @JsonProperty("group_id")
            val groupId: Long,
            @JsonProperty("nickname")
            val nickname: String,
            @JsonProperty("avatar")
            val avatar: String,
            @JsonProperty("description")
            val description: String,
        )
    }
}
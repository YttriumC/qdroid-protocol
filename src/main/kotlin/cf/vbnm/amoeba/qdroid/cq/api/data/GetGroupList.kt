package cf.vbnm.amoeba.qdroid.cq.api.data

import cf.vbnm.amoeba.qdroid.cq.api.BaseApi
import cf.vbnm.amoeba.qdroid.cq.api.enums.Retcode
import cf.vbnm.amoeba.qdroid.cq.api.enums.Status
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.ObjectMapper

class GetGroupList(
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
    data: GroupInfoList
) : BaseApi<GetGroupList.GroupInfoList>(status, retcode, msg, wording, echo, data) {

    companion object {
        fun parseApiRet(map: Map<String, Any?>, objectMapper: ObjectMapper): BaseApi<*> {
            return objectMapper.convertValue(map, GetGroupList::class.java)
        }
    }

    class GroupInfoList : ArrayList<GroupInfo>()

    data class GroupInfo(
        @JsonProperty("group_id")
        val groupId: Long,
        @JsonProperty("group_name")
        val groupName: String,
        /**
         * 群备注
         * */
        @JsonProperty("group_memo")
        val groupMemo: String?,
        @JsonProperty("group_create_time")
        val groupCreateTime: Int,
        @JsonProperty("group_level")
        val groupLevel: String,
        @JsonProperty("member_count")
        val memberCount: Int,
        @JsonProperty("max_member_count")
        val maxMemberCount: Int,
    ) {
        val getGroupAvatarUrl
            get() = "https://p.qlogo.cn/gh/${groupId}/${groupId}/100"

    }
}
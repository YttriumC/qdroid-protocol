package cf.vbnm.amoeba.qdroid.cq.api.data

import cf.vbnm.amoeba.qdroid.cq.api.BaseApi
import cf.vbnm.amoeba.qdroid.cq.api.enums.Retcode
import cf.vbnm.amoeba.qdroid.cq.api.enums.Status
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.ObjectMapper

class GetGroupAtAllRemain(
    @JsonProperty("status") status: Status,
    @JsonProperty("retcode") retcode: Retcode,
    @JsonProperty("msg") msg: String? = null,
    @JsonProperty("wording") wording: String? = null,
    @JsonProperty("echo") echo: String? = null,
    @JsonProperty("data") data: GroupAtAllRemain
) : BaseApi<GetGroupAtAllRemain.GroupAtAllRemain>(status, retcode, msg, wording, echo, data) {

    companion object {
        fun parseApiRet(map: Map<String, Any?>, objectMapper: ObjectMapper): BaseApi<*> {
            return objectMapper.convertValue(map, GetGroupAtAllRemain::class.java)
        }
    }

    data class GroupAtAllRemain(
        @JsonProperty("can_at_all") val canAtAll: Boolean,
        @JsonProperty("remain_at_all_count_for_group") val remainAtAllCountForGroup: Short,
        @JsonProperty("remain_at_all_count_for_uin") val remainAtAllCountForUin: Short,
    )
}
package cf.vbnm.amoeba.qdroid.cq.api.data

import cf.vbnm.amoeba.qdroid.cq.api.BaseApi
import cf.vbnm.amoeba.qdroid.cq.api.enums.Retcode
import cf.vbnm.amoeba.qdroid.cq.api.enums.Status
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.ObjectMapper

class GetStrangerInfo(
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
    data: StrangerInfo
) : BaseApi<GetStrangerInfo.StrangerInfo>(status, retcode, msg, wording, echo, data) {

    companion object {
        fun parseApiRet(map: Map<String, Any?>, objectMapper: ObjectMapper): BaseApi<*> {
            return objectMapper.convertValue(map, GetStrangerInfo::class.java)
        }
    }

    data class StrangerInfo(
        @JsonProperty("user_id")
        val userId: Long,
        @JsonProperty("nickname")
        val nickname: String,
        @JsonProperty("sex")
        val sex: String,
        @JsonProperty("age")
        val age: Int,
        @JsonProperty("qid")
        val qid: String,
        @JsonProperty("level")
        val level: Int,
        @JsonProperty("login_days")
        val loginDays: Int,
    )
}
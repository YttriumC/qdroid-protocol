package cf.vbnm.amoeba.qdroid.cq.api.data

import cf.vbnm.amoeba.qdroid.cq.api.BaseApi
import cf.vbnm.amoeba.qdroid.cq.api.enums.Retcode
import cf.vbnm.amoeba.qdroid.cq.api.enums.Status
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.ObjectMapper

class GetGroupFileSystemInfo(
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
    data: GroupFileSystemInfo
) : BaseApi<GetGroupFileSystemInfo.GroupFileSystemInfo>(status, retcode, msg, wording, echo, data) {

    companion object {
        fun parseApiRet(map: Map<String, Any?>, objectMapper: ObjectMapper): BaseApi<*> {
            return objectMapper.convertValue(map, GetGroupFileSystemInfo::class.java)
        }
    }

    data class GroupFileSystemInfo(
        @JsonProperty("file_count")
        val fileCount: Long,
        @JsonProperty("limit_count")
        val limitCount: String,
        @JsonProperty("used_space")
        val usedSpace: Long,
        @JsonProperty("total_space")
        val totalSpace: Long,
    )
}
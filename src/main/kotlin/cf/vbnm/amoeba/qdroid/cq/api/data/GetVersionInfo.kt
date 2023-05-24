package cf.vbnm.amoeba.qdroid.cq.api.data

import cf.vbnm.amoeba.qdroid.cq.api.BaseApi
import cf.vbnm.amoeba.qdroid.cq.api.enums.Retcode
import cf.vbnm.amoeba.qdroid.cq.api.enums.Status
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.ObjectMapper

class GetVersionInfo(
    @JsonProperty("status") status: Status,
    @JsonProperty("retcode") retcode: Retcode,
    @JsonProperty("msg") msg: String? = null,
    @JsonProperty("wording") wording: String? = null,
    @JsonProperty("echo") echo: String? = null,
    @JsonProperty("data") data: VersionDetail
) : BaseApi<GetVersionInfo.VersionDetail>(status, retcode, msg, wording, echo, data) {

    companion object {
        fun parseApiRet(map: Map<String, Any?>, objectMapper: ObjectMapper): BaseApi<*> {
            return objectMapper.convertValue(map, GetVersionInfo::class.java)
        }
    }

    data class VersionDetail(
        @JsonProperty("app_name") val appName: String,
        @JsonProperty("app_version") val appVersion: String,
        @JsonProperty("app_full_name") val appFullName: String,
        @JsonProperty("protocol_version") val protocolVersion: String,
        @JsonProperty("coolq_edition") val coolqEdition: String,
        @JsonProperty("coolq_directory") val coolqDirectory: String,
        @JsonProperty("go-cqhttp") val goCqhttp: Boolean,
        @JsonProperty("plugin_version") val pluginVersion: String,
        @JsonProperty("plugin_build_number") val pluginBuildNumber: Int,
        @JsonProperty("plugin_build_configuration") val pluginBuildConfiguration: String,
        @JsonProperty("runtime_version") val runtimeVersion: String,
        @JsonProperty("runtime_os") val runtimeOs: String,
        @JsonProperty("version") val version: String,
        @JsonProperty("protocol") val protocol: Int,
    )
}
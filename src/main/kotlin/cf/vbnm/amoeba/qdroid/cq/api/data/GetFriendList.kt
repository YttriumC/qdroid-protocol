package cf.vbnm.amoeba.qdroid.cq.api.data

import cf.vbnm.amoeba.qdroid.cq.api.BaseApi
import cf.vbnm.amoeba.qdroid.cq.api.enums.Retcode
import cf.vbnm.amoeba.qdroid.cq.api.enums.Status
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.ObjectMapper

class GetFriendList(
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
    data: FriendInfoList
) : BaseApi<GetFriendList.FriendInfoList>(status, retcode, msg, wording, echo, data) {

    companion object {
        fun parseApiRet(map: Map<String, Any?>, objectMapper: ObjectMapper): BaseApi<*> {
            return objectMapper.convertValue(map, GetFriendList::class.java)
        }
    }

    class FriendInfoList : ArrayList<FriendInfo>()

    data class FriendInfo(
        @JsonProperty("user_id")
        val userId: Long,
        @JsonProperty("nickname")
        val nickname: String,
        @JsonProperty("remark")
        val remark: String,
    )
}
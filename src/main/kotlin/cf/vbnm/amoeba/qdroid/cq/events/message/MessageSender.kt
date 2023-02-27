package cf.vbnm.amoeba.qdroid.cq.events.message

import com.fasterxml.jackson.annotation.JsonProperty

open class MessageSender(
    @JsonProperty("user_id")
    val userId: Long,
    @JsonProperty("nickname")
    val nickname: String,
    @JsonProperty("sex")
    val sex: String,
    @JsonProperty("age")
    val age: Int,
) {
    override fun toString(): String {
        return "userId=$userId, nickname='$nickname', sex='$sex', age=$age)"
    }
}
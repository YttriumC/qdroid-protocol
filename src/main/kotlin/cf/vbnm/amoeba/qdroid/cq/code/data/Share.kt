package cf.vbnm.amoeba.qdroid.cq.code.data

import cf.vbnm.amoeba.qdroid.cq.code.BaseMsgPartial
import cf.vbnm.amoeba.qdroid.cq.code.enums.MsgPartialType
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

class Share(
    @JsonProperty("data")
    data: ShareData
) : BaseMsgPartial<Share.ShareData>(MsgPartialType.SHARE, data) {
    constructor(
        url: String,
        title: String,
        content: String? = null,
        image: String? = null,
    ) : this(ShareData(url, title, content, image))

    @JsonInclude(JsonInclude.Include.NON_NULL)
    data class ShareData(
        @JsonProperty("url")
        val url: String,
        @JsonProperty("title")
        val title: String,
        @JsonProperty("content")
        val content: String?,
        @JsonProperty("image")
        val image: String?,
    )
}
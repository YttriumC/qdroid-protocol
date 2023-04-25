package cf.vbnm.amoeba.qdroid.cq.code.data

import cf.vbnm.amoeba.qdroid.cq.code.BaseMsgPartial
import cf.vbnm.amoeba.qdroid.cq.code.enums.MsgPartialType
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

class Image(
    @JsonProperty("data")
    data: ImageData
) : BaseMsgPartial<Image.ImageData>(MsgPartialType.IMAGE, data) {
    constructor(
        file: String,
        type: String? = null,
        subType: Int? = 1,
        url: String? = null,
        cache: Int? = 1,
        id: Int? = null,
        threads: Int = 3,
    ) : this(ImageData(file, type, subType, url, cache, id, threads))

    @JsonInclude(JsonInclude.Include.NON_NULL)
    data class ImageData(
        @JsonProperty("file") val file: String,
        @JsonProperty("type") val type: String?,
        @JsonProperty("subType") val subType: Int?,
        @JsonProperty("url") val url: String?,
        @JsonProperty("cache") val cache: Int?,
        @JsonProperty("id") val id: Int?,
        @JsonProperty("c") val threads: Int
    )
}
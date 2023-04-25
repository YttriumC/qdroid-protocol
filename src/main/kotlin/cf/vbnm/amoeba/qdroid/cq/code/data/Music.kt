package cf.vbnm.amoeba.qdroid.cq.code.data

import cf.vbnm.amoeba.qdroid.cq.code.BaseMsgPartial
import cf.vbnm.amoeba.qdroid.cq.code.enums.MsgPartialType
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

class Music(
    @JsonProperty("data")
    data: MusicData
) : BaseMsgPartial<Music.MusicData>(MsgPartialType.MUSIC, data) {
    constructor(
        type: String,
        id: String? = null,
        url: String? = null,
        audio: String? = null,
        title: String? = null,
        content: String? = null,
        image: String? = null,
    ) : this(MusicData(type, id, url, audio, title, content, image))

    constructor(
        type: String,
        url: String? = null,
        audio: String? = null,
        title: String? = null,
        content: String? = null,
        image: String? = null,
    ) : this(type, null, url, audio, title, content, image)

    constructor(
        @JsonProperty("type")
        type: String,
        @JsonProperty("id")
        id: String
    ) : this(type, id, null, null, null, null, null)

    @JsonInclude(JsonInclude.Include.NON_NULL)
    data class MusicData(
        @JsonProperty("type")
        val type: String,
        @JsonProperty("id")
        val id: String?,
        @JsonProperty("url")
        val url: String?,
        @JsonProperty("audio")
        val audio: String?,
        @JsonProperty("title")
        val title: String?,
        @JsonProperty("content")
        val content: String?,
        @JsonProperty("image")
        val image: String?,
    )
}
package cf.vbnm.amoeba.qdroid.cq.code.data

import cf.vbnm.amoeba.qdroid.cq.code.BaseMsgPartial
import cf.vbnm.amoeba.qdroid.cq.code.enums.MsgPartialType
import com.fasterxml.jackson.annotation.JsonProperty

class Video(
    @JsonProperty("data")
    data: VideoData
) : BaseMsgPartial<Video.VideoData>(MsgPartialType.VIDEO, data) {
    constructor(
        file: String,
    ) : this(VideoData(file))

    data class VideoData(
        @JsonProperty("file") val file: String
    )
}
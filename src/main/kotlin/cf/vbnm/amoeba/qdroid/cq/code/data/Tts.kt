package cf.vbnm.amoeba.qdroid.cq.code.data

import cf.vbnm.amoeba.qdroid.cq.code.BaseMsgPartial
import cf.vbnm.amoeba.qdroid.cq.code.enums.MsgPartialType
import com.fasterxml.jackson.annotation.JsonProperty

class Tts(
    @JsonProperty("data")
    data: TtsData
) : BaseMsgPartial<Tts.TtsData>(MsgPartialType.TTS, data) {
    constructor(
        text: String,
    ) : this(TtsData(text))

    data class TtsData(
        @JsonProperty("text")
        val text: String
    )
}
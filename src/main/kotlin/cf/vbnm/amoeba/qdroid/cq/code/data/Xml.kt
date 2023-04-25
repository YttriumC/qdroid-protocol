package cf.vbnm.amoeba.qdroid.cq.code.data

import cf.vbnm.amoeba.qdroid.cq.code.BaseMsgPartial
import cf.vbnm.amoeba.qdroid.cq.code.enums.MsgPartialType
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

class Xml(
    @JsonProperty("data")
    data: XmlData
) : BaseMsgPartial<Xml.XmlData>(MsgPartialType.XML, data) {
    constructor(
        data: String,
        resid: Int?
    ) : this(XmlData(data, resid))

    @JsonInclude(JsonInclude.Include.NON_NULL)
    data class XmlData(
        @JsonProperty("data") val data: String,
        @JsonProperty("resid") val resid: Int?
    )
}
package cf.vbnm.amoeba.qdroid.cq.events.message

import cf.vbnm.amoeba.qdroid.cq.events.enums.MessagePartialType

data class MessagePartial(
    val type: MessagePartialType,
    val data: Map<String, String>
) {
    override fun toString(): String {
        return "MessagePartial(type=$type, data=$data)"
    }
}
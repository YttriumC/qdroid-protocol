package cf.vbnm.amoeba.qdroid.cq

import cf.vbnm.amoeba.qdroid.cq.events.enums.MessagePartialType


class MessageDetail : ArrayList<MessageDetail.MessagePartial>() {

    fun addText(text: String): MessageDetail {
        add(MessagePartial(MessagePartialType.TEXT, mutableMapOf(Pair("text", text))))
        return this
    }

    fun addAt(user: Long): MessageDetail {
        add(MessagePartial(MessagePartialType.AT, mutableMapOf(Pair("qq", user))))
        return this
    }

    fun isAt(user: Long): Boolean {
        this.forEach {
            if (it.type == MessagePartialType.AT) {
                if (it.data["qq"].toString() == user.toString()) {
                    return true
                }
            }
        }
        return false
    }

    fun getAts(): List<MessagePartial> {
        val list = ArrayList<MessagePartial>()
        forEach {
            if (it.type == MessagePartialType.AT) {
                list.add(it)
            }
        }
        return list
    }

    companion object {
        fun at(user: Long): MessagePartial {
            return MessagePartial(MessagePartialType.AT, mutableMapOf(Pair("qq", user)))

        }
    }

    data class MessagePartial(
        val type: MessagePartialType,
        val data: Map<String, Any>
    ) {
        override fun toString(): String {
            return "MessagePartial(type=$type, data=$data)"
        }

        @Suppress("UNUSED_EXPRESSION")
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as MessagePartial

            if (type != other.type) return false
            when (type) {
                MessagePartialType.TEXT -> {
                    return data["text"] == other.data["text"]
                }

                MessagePartialType.FACE -> false
                MessagePartialType.RECORD -> false
                MessagePartialType.VIDEO -> false
                MessagePartialType.AT -> {
                    return data["qq"].toString() == other.data["qq"].toString()
                }

                MessagePartialType.SHARE -> false
                MessagePartialType.MUSIC -> false
                MessagePartialType.IMAGE -> false
                MessagePartialType.REPLY -> false
                MessagePartialType.RED_BAG -> false
                MessagePartialType.POKE -> false
                MessagePartialType.GIFT -> false
                MessagePartialType.FORWARD -> false
                MessagePartialType.NODE -> false
                MessagePartialType.XML -> false
                MessagePartialType.JSON -> false
                MessagePartialType.CARD_IMAGE -> false
                MessagePartialType.TTS -> false
            }
            return false
        }

        @Deprecated("Not complete", ReplaceWith("equals"))
        override fun hashCode(): Int {
            return type.hashCode()
        }

    }

    fun getText(): String {
        return StringBuilder().run {
            this@MessageDetail.forEach {
                append(
                    when (it.type) {
                        MessagePartialType.TEXT -> {
                            it.data["text"]
                        }

                        MessagePartialType.FACE -> {
                            ""
                        }

                        MessagePartialType.RECORD -> {
                            ""
                        }

                        MessagePartialType.VIDEO -> {
                            ""
                        }

                        MessagePartialType.AT -> {
                            ""
                        }

                        MessagePartialType.SHARE -> {
                            ""
                        }

                        MessagePartialType.MUSIC -> {
                            ""
                        }

                        MessagePartialType.IMAGE -> {
                            ""
                        }

                        MessagePartialType.REPLY -> {
                            ""
                        }

                        MessagePartialType.RED_BAG -> {
                            ""
                        }

                        MessagePartialType.POKE -> {
                            ""
                        }

                        MessagePartialType.GIFT -> {
                            ""
                        }

                        MessagePartialType.FORWARD -> {
                            ""
                        }

                        MessagePartialType.NODE -> {
                            ""
                        }

                        MessagePartialType.XML -> {
                            ""
                        }

                        MessagePartialType.JSON -> {
                            ""
                        }

                        MessagePartialType.CARD_IMAGE -> {
                            ""
                        }

                        MessagePartialType.TTS -> {
                            ""
                        }
                    }
                )
            }
            toString()
        }
    }
}



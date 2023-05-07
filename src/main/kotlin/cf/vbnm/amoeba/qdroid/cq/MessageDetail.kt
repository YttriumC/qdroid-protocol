package cf.vbnm.amoeba.qdroid.cq

import cf.vbnm.amoeba.qdroid.cq.code.BaseMsgPartial
import cf.vbnm.amoeba.qdroid.cq.code.data.At
import cf.vbnm.amoeba.qdroid.cq.code.data.Reply
import cf.vbnm.amoeba.qdroid.cq.code.data.Text
import cf.vbnm.amoeba.qdroid.cq.code.enums.MsgPartialType
import kotlin.jvm.optionals.getOrNull


class MessageDetail(capacity: Int) : ArrayList<BaseMsgPartial<*>>(capacity) {
    constructor() : this(16)

    private var _hasReply = false

    val hasReply: Boolean get() = _hasReply

    constructor(collection: Collection<BaseMsgPartial<*>>) : this(collection.size + 16) {
        collection.forEach { add(it) }
    }

    override fun add(element: BaseMsgPartial<*>): Boolean {
        if (element.type == MsgPartialType.REPLY) {
            if (_hasReply) return false
            return super.add(element)
        }
        return super.add(element)
    }

    override fun add(index: Int, element: BaseMsgPartial<*>) {
        if (element.type == MsgPartialType.REPLY) {
            if (_hasReply) return
            return super.add(index, element)
        }
        super.add(index, element)
    }

    fun addNotReply(element: BaseMsgPartial<*>): MessageDetail {
        add(element)
        return this
    }

    override fun addAll(elements: Collection<BaseMsgPartial<*>>): Boolean {
        elements.forEach { add(it) }
        return true
    }

    override fun set(index: Int, element: BaseMsgPartial<*>): BaseMsgPartial<*> {
        if (this[index].type == MsgPartialType.REPLY) {
            _hasReply = element.type == MsgPartialType.REPLY
            return super.set(index, element)
        } else {
            if (hasReply && element.type == MsgPartialType.REPLY) {
                return element
            }
            return super.set(index, element)
        }
    }

    fun addText(text: String): MessageDetail {
        add(Text(text))
        return this
    }

    fun addReply(id: Int): MessageDetail {
        if (_hasReply) throw IllegalArgumentException("Already had a reply")
        add(Reply(id))
        _hasReply = true
        return this
    }

    fun addReplyOrNot(id: Int): MessageDetail {
        if (_hasReply) return this
        add(Reply(id))
        _hasReply = true
        return this
    }

    fun getReply(): Reply? {
        return MsgPartialType.REPLY.toTypeOrNull(
            stream().filter { it.type == MsgPartialType.REPLY }.findFirst().getOrNull()
        )
    }

    fun addAt(user: Long): MessageDetail {
        add(At(user.toString()))
        return this
    }

    fun addAtAll(): MessageDetail {
        add(At("all"))
        return this
    }

    fun isAt(user: Long): Boolean {
        this.forEach { partial ->
            if (partial.type == MsgPartialType.AT) {
                MsgPartialType.AT.toType(partial).let {
                    if (it.data.qq == user.toString()) {
                        return true
                    }
                }

            }
        }
        return false
    }

    fun getAts(): List<At> {
        val list = ArrayList<At>()
        forEach { partial ->
            MsgPartialType.AT.toTypeOrNull(partial)?.let { list.add(it) }
        }
        return list
    }

    fun addMsg(baseMsgPartial: BaseMsgPartial<*>): MessageDetail {
        add(baseMsgPartial)
        return this
    }

    companion object {

        fun of(vararg baseMsgPartial: BaseMsgPartial<*>): MessageDetail {
            return MessageDetail().apply {
                this.addAll(baseMsgPartial)
            }
        }

        fun oneText(charSequence: CharSequence): MessageDetail {
            return MessageDetail().addText(charSequence.toString())
        }
    }

    fun getText(): String {
        return StringBuilder().run {
            this@MessageDetail.forEach {
                append(
                    when (it.type) {
                        MsgPartialType.TEXT -> {
                            MsgPartialType.TEXT.toType(it).data.text
                        }

                        else -> ""
                    }
                )
            }
            toString()
        }
    }

    override fun toString(): String {
        return StringBuilder().also {
            this.forEach { part ->
                it.append(
                    when (part.type) {
                        MsgPartialType.TEXT -> {
                            MsgPartialType.TEXT.toType(part).data.text
                        }

                        MsgPartialType.IMAGE -> {
                            "{image: ${MsgPartialType.IMAGE.toType(part).data.url ?: MsgPartialType.IMAGE.toType(part).data.file}}"
                        }

                        MsgPartialType.VIDEO -> {
                            "{video: ${MsgPartialType.VIDEO.toType(part).data.file}}"
                        }

                        MsgPartialType.AT -> {
                            "{at: ${MsgPartialType.AT.toType(part).data.qq}}"
                        }

                        MsgPartialType.FACE -> {
                            "{face: ${MsgPartialType.FACE.toType(part).data.id}}"
                        }

                        MsgPartialType.REPLY -> {
                            "{reply: ${MsgPartialType.REPLY.toType(part).data.id}}"
                        }

                        else -> ""
                    }
                )
            }
        }.toString()
    }

}



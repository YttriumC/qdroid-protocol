package cf.vbnm.amoeba.qdroid.bot.statistics

import cf.vbnm.amoeba.qdroid.cq.events.BasePostEvent
import cf.vbnm.amoeba.qdroid.cq.events.enums.PostType
import java.util.concurrent.atomic.AtomicInteger

class Statistics {
    private val _totalSentApi = AtomicInteger(0)
    private val _totalSuccessApi = AtomicInteger(0)
    private val _totalFailedApi = AtomicInteger(0)
    private val _totalReceivedEvent = AtomicInteger(0)
    private val _totalReceivedMetaEvent = AtomicInteger(0)
    private val _totalReceivedMsgEvent = AtomicInteger(0)
    private val _totalReceivedNoticeEvent = AtomicInteger(0)
    private val _totalReceivedRequestEvent = AtomicInteger(0)
    private val _totalDroppedApi = AtomicInteger(0)

    val totalSentApi: Int get() = _totalSentApi.get()
    val totalSuccessApi: Int get() = _totalSuccessApi.get()
    val totalFailedApi: Int get() = _totalFailedApi.get()
    val totalReceivedEvent: Int get() = _totalReceivedEvent.get()
    val totalReceivedMetaEvent: Int get() = _totalReceivedMetaEvent.get()
    val totalReceivedMsgEvent: Int get() = _totalReceivedMsgEvent.get()
    val totalReceivedNoticeEvent: Int get() = _totalReceivedNoticeEvent.get()
    val totalReceivedRequestEvent: Int get() = _totalReceivedRequestEvent.get()
    val totalDroppedApi: Int get() = _totalDroppedApi.get()


    fun addSentApi() {
        _totalSentApi.incrementAndGet()
        global._totalSentApi.incrementAndGet()
    }

    fun addSuccessApi() {
        _totalSuccessApi.incrementAndGet()
        global._totalSuccessApi.incrementAndGet()
    }

    fun addFailedApi() {
        _totalFailedApi.incrementAndGet()
        global._totalFailedApi.incrementAndGet()
    }

    private fun addReceivedEvent() {
        _totalReceivedEvent.incrementAndGet()
        global._totalReceivedEvent.incrementAndGet()
    }

    fun addReceivedEvent(event: BasePostEvent) {
        _totalReceivedEvent.incrementAndGet()
        global._totalReceivedEvent.incrementAndGet()
        when (event.postType) {
            PostType.MESSAGE, PostType.MESSAGE_SENT -> {
                _totalReceivedMsgEvent.incrementAndGet()
                global._totalReceivedMsgEvent.incrementAndGet()
            }

            PostType.REQUEST -> {
                _totalReceivedRequestEvent.incrementAndGet()
                global._totalReceivedRequestEvent.incrementAndGet()
            }

            PostType.NOTICE -> {
                _totalReceivedNoticeEvent.incrementAndGet()
                global._totalReceivedNoticeEvent.incrementAndGet()
            }

            PostType.META_EVENT -> {
                _totalReceivedMetaEvent.incrementAndGet()
                global._totalReceivedMetaEvent.incrementAndGet()
            }
        }
    }

    fun addReceivedMsgEvent() {
        _totalReceivedMsgEvent.incrementAndGet()
        _totalReceivedEvent.incrementAndGet()
        global._totalReceivedMsgEvent.incrementAndGet()
        global._totalReceivedEvent.incrementAndGet()
    }

    fun addReceivedRequestEvent() {
        _totalReceivedRequestEvent.incrementAndGet()
        _totalReceivedEvent.incrementAndGet()
        global._totalReceivedRequestEvent.incrementAndGet()
        global._totalReceivedEvent.incrementAndGet()
    }

    fun addReceivedNoticeEvent() {
        _totalReceivedNoticeEvent.incrementAndGet()
        _totalReceivedEvent.incrementAndGet()
        global._totalReceivedNoticeEvent.incrementAndGet()
        global._totalReceivedEvent.incrementAndGet()
    }

    fun addReceivedMetaEvent() {
        _totalReceivedMetaEvent.incrementAndGet()
        _totalReceivedEvent.incrementAndGet()
        global._totalReceivedMetaEvent.incrementAndGet()
        global._totalReceivedEvent.incrementAndGet()
    }


    fun addDroppedApi() {
        _totalDroppedApi.incrementAndGet()
        global._totalDroppedApi.incrementAndGet()
    }


    companion object {
        val global = Statistics()
    }
}
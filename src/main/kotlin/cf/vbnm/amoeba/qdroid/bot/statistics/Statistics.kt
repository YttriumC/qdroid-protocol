package cf.vbnm.amoeba.qdroid.bot.statistics

import org.springframework.stereotype.Component
import java.util.concurrent.atomic.AtomicInteger

@Component
class Statistics private constructor() {
    private var totalSent = AtomicInteger(0)
    private var totalReceivedEvent = AtomicInteger(0)
    private var totalReceivedMetaEvent = AtomicInteger(0)
    private var totalReceivedMsgEvent = AtomicInteger(0)
    private var totalReceivedNoticeEvent = AtomicInteger(0)
    private var totalReceivedRequestEvent = AtomicInteger(0)

    private var totalDroppedApi = AtomicInteger(0)

    fun getTotalSent(): Int {
        return totalSent.get()
    }

    fun getTotalReceived(): Int {
        return totalReceivedEvent.get()
    }

    fun getTotalDropped(): Int {
        return totalDroppedApi.get()
    }


    fun addSent(): Int {
        return totalSent.incrementAndGet()
    }

    fun addReceived(): Int {
        return totalReceivedEvent.incrementAndGet()
    }

    fun addDropped(): Int {
        return totalDroppedApi.incrementAndGet()
    }

    companion object {
        val Statistics = Statistics()

    }
}
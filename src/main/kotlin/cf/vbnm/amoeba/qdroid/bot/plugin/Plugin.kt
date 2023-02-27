package cf.vbnm.amoeba.qdroid.bot.plugin

import cf.vbnm.amoeba.qdroid.bot.QBot
import cf.vbnm.amoeba.qdroid.cq.events.BasePostEvent

interface Plugin<T : BasePostEvent> {

    /**
     * @return true 表示执行完毕, 后续任务不再执行, false表示未执行完, 将执行后续任务
     * */
    fun apply(bot: QBot, event: T): Boolean

    fun isTargetEvent(event: BasePostEvent): Boolean {
        return try {
            @Suppress("UNCHECKED_CAST")
            event as T
            true
        } catch (_: Exception) {
            false
        }
    }

}
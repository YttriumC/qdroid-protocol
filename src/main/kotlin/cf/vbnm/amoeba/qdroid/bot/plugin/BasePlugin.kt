package cf.vbnm.amoeba.qdroid.bot.plugin

import cf.vbnm.amoeba.core.CoreProperty
import cf.vbnm.amoeba.qdroid.bot.QBot
import cf.vbnm.amoeba.qdroid.cq.events.BasePostEvent
import org.quartz.Scheduler
import kotlin.reflect.KClass

abstract class BasePlugin<T : BasePostEvent>(
    protected val coreProperty: CoreProperty,
    protected val scheduler: Scheduler,
    protected val pluginMessageFilter: PluginMessageFilter
) {
    private val abort = ThreadLocal.withInitial { false }

    /**
     * For reflect use, against type erasure
     * */
    abstract fun getTypeParameterClass(): KClass<*>

    /**
     * @return true 表示执行完毕, 后续任务不再执行, false表示未执行完, 将执行后续任务
     * */
    abstract fun apply(bot: QBot, event: T)

    fun abortFilter() {
        abort.set(true)
    }

    @Suppress("UNUSED")
    fun handleEvent(bot: QBot, event: T): Boolean {
        apply(bot, event)
        return abort.get().also { abort.remove() }
    }

    fun PluginMessageInterceptor.finished() {
        pluginMessageFilter.removeInterceptor(this)
    }


}
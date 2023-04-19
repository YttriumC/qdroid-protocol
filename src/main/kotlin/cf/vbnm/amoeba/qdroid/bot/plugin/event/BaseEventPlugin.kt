package cf.vbnm.amoeba.qdroid.bot.plugin.event

import cf.vbnm.amoeba.core.CoreProperty
import cf.vbnm.amoeba.qdroid.bot.QBot
import cf.vbnm.amoeba.qdroid.cq.events.BasePostEvent
import org.quartz.Scheduler
import kotlin.reflect.KClass

abstract class BaseEventPlugin<T : BasePostEvent>(
    protected open val coreProperty: CoreProperty,
    protected val scheduler: Scheduler,
    protected val pluginMessageFilter: PluginMessageFilter
) {
    private val abort = ThreadLocal.withInitial { false }

    /**
     * For reflect use, against type erasure
     * */
    abstract fun getTypeParameterClass(): KClass<T>


    abstract suspend fun apply(bot: QBot, event: T)

    /**
     * 调用后表示执行完毕, 后续任务不再执行,未调用则表示未执行完, 将执行后续任务
     * */
    fun abortFilter() {
        abort.set(true)
    }

    @Suppress("UNUSED")
    suspend fun handleEvent(bot: QBot, event: T): Boolean {
        apply(bot, event)
        return abort.get().also { abort.remove() }
    }

    fun PluginMessageInterceptor.finished() {
        pluginMessageFilter.removeInterceptor(this)
    }

}
package cf.vbnm.amoeba.qdroid.bot.plugin.event

import cf.vbnm.amoeba.core.CoreProperty
import cf.vbnm.amoeba.qdroid.bot.QBot
import cf.vbnm.amoeba.qdroid.bot.plugin.AbstractPlugin
import cf.vbnm.amoeba.qdroid.cq.events.BasePostEvent
import kotlinx.coroutines.asContextElement
import kotlinx.coroutines.withContext
import org.quartz.Scheduler
import org.springframework.beans.factory.annotation.Autowired
import kotlin.reflect.KClass

abstract class BaseEventPlugin<T : BasePostEvent>(
    protected open val coreProperty: CoreProperty,
    protected val pluginMessageFilter: PluginMessageFilter
) : AbstractPlugin() {
    private val abort = ThreadLocal.withInitial { true }
    private lateinit var _scheduler: Scheduler

    protected val scheduler: Scheduler get() = _scheduler

    /**
     * For reflect use, against type erasure
     * */
    abstract fun getTypeParameterClass(): KClass<T>


    abstract suspend fun apply(bot: QBot, event: T)

    @Autowired
    fun setScheduler(scheduler: Scheduler) {
        this._scheduler = scheduler
    }

    /**
     * 未调用表示执行完毕, 后续任务不再执行,调用后则表示未执行完, 将执行后续任务
     * */
    fun nextFilter() {
        abort.set(false)
    }

    @Suppress("UNUSED")
    suspend fun handleEvent(bot: QBot, event: T): Boolean {
        return withContext(abort.asContextElement()) {
            abort.set(true)
            apply(bot, event)
            abort.get()
        }
    }

    fun PluginMessageInterceptor.finished() {
        pluginMessageFilter.removeInterceptor(this)
    }

}
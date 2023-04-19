package cf.vbnm.amoeba.qdroid.bot.plugin

import cf.vbnm.amoeba.core.log.Slf4kt
import cf.vbnm.amoeba.qdroid.bot.QBot
import cf.vbnm.amoeba.qdroid.bot.plugin.event.BaseEventPlugin
import cf.vbnm.amoeba.qdroid.bot.plugin.event.PluginMessageFilter
import cf.vbnm.amoeba.qdroid.cq.events.BasePostEvent
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.springframework.stereotype.Component
import kotlin.reflect.full.callSuspend

private val log = Slf4kt.getLogger(EventPluginManager::class.java)

@Component
class EventPluginManager(
    private val plugins: MutableList<BaseEventPlugin<*>>,
    private val pluginMessageFilter: PluginMessageFilter
) {

    init {
        plugins.sortBy { baseEventPlugin ->
            baseEventPlugin::class.annotations.filterIsInstance<PluginOrder>().forEach {
                return@sortBy it.order
            }
            return@sortBy 500
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun handleEvent(event: BasePostEvent, bot: QBot) {
        GlobalScope.launch {
            if (event.isMessage()) {
                if (pluginMessageFilter.consumeMessage(event.toMessage(), bot)) {
                    return@launch
                }
            }
            try {
                plugins.forEach { plugin ->
                    if (plugin.getTypeParameterClass().isInstance(event)) {
                        plugin::class.members.filter { it.name == "handleEvent" }.forEach { kCallable ->
                            try {
                                if (kCallable.callSuspend(plugin, bot, event) == true) {
                                    return@launch
                                }
                            } catch (e: ClassCastException) {
                                log.warn("Wrong target event: event type mismatch")
                            } catch (e: Exception) {
                                log.warn("Plugin occurred an exception: ", e)
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                log.warn("Event handler process has an exception", e)
            }
        }
    }


}

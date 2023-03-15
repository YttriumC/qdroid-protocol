package cf.vbnm.amoeba.qdroid.bot.plugin

import cf.vbnm.amoeba.core.log.Slf4kt
import cf.vbnm.amoeba.qdroid.bot.QBot
import cf.vbnm.amoeba.qdroid.cq.events.BasePostEvent
import org.springframework.core.annotation.AnnotationUtils
import org.springframework.stereotype.Component

private val log = Slf4kt.getLogger(PluginManager::class.java)

@Component
class PluginManager(
    private val plugins: MutableList<BasePlugin<*>>,
    private val pluginMessageFilter: PluginMessageFilter
) {
    init {
        plugins.sortBy { it ->
            AnnotationUtils.getAnnotation(it.javaClass, PluginTarget::class.java)?.let {
                return@sortBy it.order
            }
            return@sortBy 100
        }
    }


    fun handleEvent(event: BasePostEvent, bot: QBot) {
        if (event.isMessage()) {
            if (pluginMessageFilter.consumeMessage(event.toMessage(), bot)) {
                return
            }
        }
        try {
            plugins.forEach { plugin ->
                if (plugin.getTypeParameterClass().isInstance(event)) {
                    plugin::class.members.filter { it.name == "handleEvent" }.forEach { kCallable ->
                        try {
                            if (kCallable.call(plugin, bot, event) == true) {
                                return@handleEvent
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

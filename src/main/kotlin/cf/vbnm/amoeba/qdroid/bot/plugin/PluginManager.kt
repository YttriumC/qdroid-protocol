package cf.vbnm.amoeba.qdroid.bot.plugin

import cf.vbnm.amoeba.core.log.Slf4kt
import cf.vbnm.amoeba.qdroid.bot.QBot
import cf.vbnm.amoeba.qdroid.cq.events.BasePostEvent
import org.springframework.core.annotation.AnnotationUtils
import org.springframework.stereotype.Component

private val log = Slf4kt.getLogger(PluginManager::class.java)

@Component
class PluginManager(private val plugins: MutableList<Plugin<*>>) {
    init {
        plugins.sortBy { it ->
            AnnotationUtils.getAnnotation(it.javaClass, PluginTarget::class.java)?.let {
                return@sortBy it.order
            }
            return@sortBy 100
        }
    }

    fun handleEvent(event: BasePostEvent, bot: QBot) {
        try {
            plugins.forEach { plugin ->
                if (plugin.isTargetEvent(event)) {
                    plugin::class.members.forEach {
                        if (it.name == "apply") {
                            if (it.call(bot, event) == true) {
                                return@handleEvent
                            }
                        }
                    }
                }
            }
        } catch (e: Exception) {
            log.warn("Event handler process has an exception", e)
        }
    }
}

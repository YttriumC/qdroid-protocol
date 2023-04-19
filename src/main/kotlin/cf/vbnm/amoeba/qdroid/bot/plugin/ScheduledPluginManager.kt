package cf.vbnm.amoeba.qdroid.bot.plugin

import cf.vbnm.amoeba.core.log.Slf4kt
import cf.vbnm.amoeba.qdroid.bot.plugin.scheduled.BaseScheduledPlugin
import org.springframework.stereotype.Component

private val log = Slf4kt.getLogger(ScheduledPluginManager::class.java)

@Component
class ScheduledPluginManager(private val plugins: List<BaseScheduledPlugin>) {

    init {
        plugins.forEach {
            it.initTrigger()
        }
    }

}
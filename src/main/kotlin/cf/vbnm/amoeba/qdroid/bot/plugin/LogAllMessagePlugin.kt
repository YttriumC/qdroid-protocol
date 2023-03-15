package cf.vbnm.amoeba.qdroid.bot.plugin

import cf.vbnm.amoeba.core.CoreProperty
import cf.vbnm.amoeba.core.log.Slf4kt
import cf.vbnm.amoeba.qdroid.bot.QBot
import cf.vbnm.amoeba.qdroid.cq.events.Message
import com.fasterxml.jackson.databind.ObjectMapper
import org.quartz.Scheduler
import org.springframework.stereotype.Component
import kotlin.reflect.KClass

private val log = Slf4kt.getLogger(LogAllMessagePlugin::class.java)

@Component
class LogAllMessagePlugin(
    coreProperty: CoreProperty,
    scheduler: Scheduler,
    pluginMessageFilter: PluginMessageFilter,
    private val objectMapper: ObjectMapper
) :
    BasePlugin<Message>(
        coreProperty,
        scheduler, pluginMessageFilter
    ) {
    override fun getTypeParameterClass(): KClass<*> = Message::class

    override fun apply(bot: QBot, event: Message) {
        log.info("Plugin print message: {}", objectMapper.writeValueAsString(event.message))
    }
}
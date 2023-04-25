package cf.vbnm.amoeba.qdroid.bot.plugin.event

import cf.vbnm.amoeba.core.CoreProperty
import cf.vbnm.amoeba.core.log.Slf4kt
import cf.vbnm.amoeba.qdroid.bot.QBot
import cf.vbnm.amoeba.qdroid.bot.plugin.PluginOrder
import cf.vbnm.amoeba.qdroid.cq.events.Message
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Component

private val log = Slf4kt.getLogger(LogAllMessageEventPlugin::class.java)

@Component
@PluginOrder(1)
class LogAllMessageEventPlugin(
    coreProperty: CoreProperty,
    pluginMessageFilter: PluginMessageFilter,
    private val objectMapper: ObjectMapper
) : BaseEventPlugin<Message>(
    coreProperty, pluginMessageFilter
) {
    override fun getTypeParameterClass() = Message::class

    override suspend fun apply(bot: QBot, event: Message) {
        log.info("Plugin print message: {}", objectMapper.writeValueAsString(event.message))
        nextFilter()
    }
}
package cf.vbnm.amoeba.qdroid.bot.plugin.event

import cf.vbnm.amoeba.core.CoreProperty
import cf.vbnm.amoeba.core.log.Slf4kt
import cf.vbnm.amoeba.qdroid.bot.QBot
import cf.vbnm.amoeba.qdroid.bot.plugin.PluginOrder
import cf.vbnm.amoeba.qdroid.cq.events.Message
import org.springframework.stereotype.Component

private val log = Slf4kt.getLogger(LogAllMessageEventPlugin::class.java)

@Component
@PluginOrder(1)
class LogAllMessageEventPlugin(
    coreProperty: CoreProperty,
    pluginMessageFilter: PluginMessageFilter
) : BaseEventPlugin<Message>(
    coreProperty, pluginMessageFilter
) {
    override fun getTypeParameterClass() = Message::class

    override suspend fun apply(bot: QBot, event: Message) {
        event.ifPrivateMessage {
            log.info("Message: From:{} :{}", it.sender.nickname, it.message.toString())
        }
        event.ifGroupMessage {

            log.info(
                "Message: Group:{}, from:{} :{}",
                bot.getGroupInfo(it.groupId).data.groupName,
                it.sender.nickname,
                it.message.toString()
            )
        }
        nextPlugin()
    }
}
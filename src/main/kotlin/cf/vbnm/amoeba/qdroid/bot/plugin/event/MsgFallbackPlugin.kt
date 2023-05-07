package cf.vbnm.amoeba.qdroid.bot.plugin.event

import cf.vbnm.amoeba.core.CoreProperty
import cf.vbnm.amoeba.qdroid.bot.QBot
import cf.vbnm.amoeba.qdroid.bot.plugin.PluginOrder
import cf.vbnm.amoeba.qdroid.cq.MessageDetail
import cf.vbnm.amoeba.qdroid.cq.events.Message
import org.springframework.stereotype.Component
import kotlin.reflect.KClass

@Component
@PluginOrder(1000)
class MsgFallbackPlugin(
    coreProperty: CoreProperty,
    pluginMessageFilter: PluginMessageFilter
) : BaseEventPlugin<Message>(coreProperty, pluginMessageFilter) {
    override fun getTypeParameterClass(): KClass<Message> {
        return Message::class
    }

    override suspend fun apply(bot: QBot, event: Message) {
        event.ifPrivateMessage {
            event.reply(bot, MessageDetail.oneText("我是qdroid, 可以使用 menu /menu 或 菜单 查看支持的功能"))
        }
        event.ifGroupMessage {
            if (it.message.isAt(bot.selfId)) {
                event.reply(bot, MessageDetail.oneText("我是qdroid, 可以使用 menu /menu 或 菜单 查看支持的功能"))
            }
        }
    }
}
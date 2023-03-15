package cf.vbnm.amoeba.qdroid.bot.plugin

import cf.vbnm.amoeba.core.CoreProperty
import cf.vbnm.amoeba.qdroid.bot.QBot
import cf.vbnm.amoeba.qdroid.cq.MessageDetail
import cf.vbnm.amoeba.qdroid.cq.events.Message
import cf.vbnm.amoeba.qdroid.cq.events.message.GroupMessage
import cf.vbnm.amoeba.qdroid.cq.events.message.PrivateMessage
import org.quartz.Scheduler
import org.springframework.stereotype.Component
import kotlin.reflect.KClass

@Component
class HelloWorldPlugin(coreProperty: CoreProperty, scheduler: Scheduler, pluginMessageFilter: PluginMessageFilter) :
    BasePlugin<Message>(
        coreProperty, scheduler,
        pluginMessageFilter
    ) {
    override fun getTypeParameterClass(): KClass<*> {
        return Message::class
    }

    override fun apply(bot: QBot, event: Message) {
        if (event.isPrivateMessage()) {
            event as PrivateMessage
            event.tempSource?.let {
                bot.sendPrivateMsg(event.userId, event.sender.groupId, MessageDetail().apply {
                    addText("Hello: ")
                    addText("\r\necho: ")
                    addText(event.message.getText())
                }) {}
            }
            abortFilter()
            return
        }
        if (event.message.isAt(bot.selfId)) {
            if (event.isGroupMessage()) {
                event as GroupMessage
                bot.sendGroupMsg(event.groupId, MessageDetail().apply {
                    addText("Hello: ")
                    addAt(event.userId)
                    addText("\r\necho: ")
                    addText(event.message.getText())
                }) {}
            }
            abortFilter()
        }
    }
}
package cf.vbnm.amoeba.qdroid.bot.plugin.event

import cf.vbnm.amoeba.core.CoreProperty
import cf.vbnm.amoeba.core.log.Slf4kt
import cf.vbnm.amoeba.qdroid.bot.QBot
import cf.vbnm.amoeba.qdroid.cq.MessageDetail
import cf.vbnm.amoeba.qdroid.cq.events.Message
import com.google.common.base.Splitter
import org.quartz.Scheduler
import kotlin.reflect.KClass

private val log = Slf4kt.getLogger(MessageCommandPlugin::class.java)

abstract class MessageCommandPlugin(
    coreProperty: CoreProperty,
    scheduler: Scheduler,
    pluginMessageFilter: PluginMessageFilter
) : BaseEventPlugin<Message>(
    coreProperty,
    scheduler, pluginMessageFilter
) {

    abstract fun getPrefixes(): Array<String>
    abstract suspend fun handle(bot: QBot, event: Message)
    fun removePrefix(str: String): String {
        var s = str
        getPrefixes().forEach {
            val res = s.removePrefix(it)
            if (res.length == s.length) {
                s = res
            } else
                return res
        }
        return s
    }

    override fun getTypeParameterClass(): KClass<Message> {
        return Message::class
    }

    override suspend fun apply(bot: QBot, event: Message) {
        if (event.isGroupMessage()) {
            if (!event.message.isAt(bot.selfId)) {
                return
            }
        }
        val part = Splitter.on(' ').omitEmptyStrings().splitToList(event.message.getText())
        if (part.size == 0) {
            return
        }
        if (getPrefixes().contains(part[0])) {
            try {
                handle(bot, event)
            } catch (e: Exception) {
                event.reply(bot, MessageDetail.oneText("出错啦: ${e.message}"))
                log.warn("An exception occurred: ", e)
            }
            abortFilter()
        }
    }
}
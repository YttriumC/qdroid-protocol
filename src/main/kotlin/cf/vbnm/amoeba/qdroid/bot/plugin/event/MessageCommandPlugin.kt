package cf.vbnm.amoeba.qdroid.bot.plugin.event

import cf.vbnm.amoeba.core.CoreProperty
import cf.vbnm.amoeba.core.log.Slf4kt
import cf.vbnm.amoeba.qdroid.bot.QBot
import cf.vbnm.amoeba.qdroid.bot.plugin.event.msgcmd.BaseMessageCommand
import cf.vbnm.amoeba.qdroid.cq.MessageDetail
import cf.vbnm.amoeba.qdroid.cq.events.Message
import com.google.common.base.Splitter
import kotlinx.coroutines.*
import org.springframework.stereotype.Component
import kotlin.reflect.KClass

private val log = Slf4kt.getLogger(MessageCommandPlugin::class.java)

@Component
class MessageCommandPlugin(
    coreProperty: CoreProperty,
    pluginMessageFilter: PluginMessageFilter,
    private val messageCommands: List<BaseMessageCommand>
) : BaseEventPlugin<Message>(
    coreProperty,
    pluginMessageFilter
) {
    private val commands: ArrayList<String> = ArrayList()

    init {
        messageCommands.forEach {
            commands.addAll(it.getPrefixes())
        }
    }

    override fun getPluginName(): String = "baseMsg"

    override fun getTypeParameterClass(): KClass<Message> {
        return Message::class
    }

    override suspend fun apply(bot: QBot, event: Message) {
        @OptIn(DelicateCoroutinesApi::class)
        GlobalScope.launch(currentCoroutineContext(), CoroutineStart.DEFAULT) {
            bot.markMsgAsRead(event.messageId)
        }
        val part = Splitter.on(' ').omitEmptyStrings().splitToList(event.message.getText().trimStart())
        if (part.size == 0) {
            nextPlugin()
            return
        }
        if (!commands.contains(part[0])) {
            nextPlugin()
            return
        }
        this["needAt"]?.let {
            if (event.isGroupMessage()) {
                if (!event.message.isAt(bot.selfId)) {
                    nextPlugin()
                    return
                }
            }
        }
        var willNext = true
        messageCommands.forEach {
            if (it.getPrefixes().contains(part[0])) {
                try {
                    if (!it.apply(bot, event)) {
                        return@apply
                    }
                } catch (e: Exception) {
                    event.reply(bot, MessageDetail.oneText("出错啦: ${e.message}").addReply(event.messageId))
                    log.warn("An exception occurred: ", e)
                }
                willNext = false
            }
        }
        if (willNext)
            nextPlugin()
    }
}


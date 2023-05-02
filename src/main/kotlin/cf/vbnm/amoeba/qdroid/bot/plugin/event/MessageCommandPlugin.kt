package cf.vbnm.amoeba.qdroid.bot.plugin.event

import cf.vbnm.amoeba.core.CoreProperty
import cf.vbnm.amoeba.core.log.Slf4kt
import cf.vbnm.amoeba.qdroid.bot.QBot
import cf.vbnm.amoeba.qdroid.cq.MessageDetail
import cf.vbnm.amoeba.qdroid.cq.events.Message
import cf.vbnm.amoeba.web.service.PropertyService
import com.google.common.base.Splitter
import kotlinx.coroutines.asContextElement
import kotlinx.coroutines.withContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import kotlin.reflect.KClass

private val log = Slf4kt.getLogger(MessageCommandPlugin::class.java)

@Component
class MessageCommandPlugin(
    coreProperty: CoreProperty,
    pluginMessageFilter: PluginMessageFilter,
    private val messageCommands: List<MessageCommand>
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
        val part = Splitter.on(' ').omitEmptyStrings().splitToList(event.message.getText().trimStart())
        if (part.size == 0) {
            nextFilter()
            return
        }
        if (!commands.contains(part[0])) {
            nextFilter()
            return
        }
        this["needAt"]?.let {
            if (event.isGroupMessage()) {
                if (!event.message.isAt(bot.selfId)) {
                    nextFilter()
                    return
                }
            }
        }
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
            }
        }
        nextFilter()
    }
}

@Component
abstract class MessageCommand {
    @Autowired
    protected lateinit var pluginMessageFilter: PluginMessageFilter

    @Autowired
    protected lateinit var propertyService: PropertyService

    private val next = ThreadLocal<Boolean>()

    open fun getPluginName(): String {
        return this::class.simpleName ?: "unmapped"
    }

    operator fun set(name: String, value: String) {
        propertyService["plugin.msgCommand.${getPluginName()}.${name.trim().removePrefix(".")}"] = value
    }

    operator fun get(name: String): String? {
        return propertyService["plugin.msgCommand.${getPluginName()}.${name.trim().removePrefix(".")}"]
    }

    fun nextFilter() {
        next.set(false)
    }

    abstract fun getPrefixes(): Array<String>

    abstract suspend fun handle(bot: QBot, msg: Message)

    suspend fun apply(bot: QBot, msg: Message): Boolean {
        return withContext(next.asContextElement(true)) {
            next.set(true)
            handle(bot, msg)
            next.get()
        }
    }

    fun removePrefix(str: String): String {
        var s = str.trimStart()
        getPrefixes().forEach {
            val res = s.removePrefix("$it ")
            if (res.length == s.length) {
                s = res
            } else
                return res
        }
        return s.trimStart()
    }
}
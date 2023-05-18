package cf.vbnm.amoeba.qdroid.bot.plugin.event.msgcmd

import cf.vbnm.amoeba.core.log.Slf4kt
import cf.vbnm.amoeba.qdroid.bot.QBot
import cf.vbnm.amoeba.qdroid.bot.plugin.AbstractPlugin
import cf.vbnm.amoeba.qdroid.bot.plugin.event.PluginMessageFilter
import cf.vbnm.amoeba.qdroid.cq.events.Message
import cf.vbnm.amoeba.web.service.PropertyService
import kotlinx.coroutines.asContextElement
import kotlinx.coroutines.withContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

private val log = Slf4kt.getLogger(BaseMessageCommand::class.java)

@Component
abstract class BaseMessageCommand {
    @Autowired
    protected lateinit var pluginMessageFilter: PluginMessageFilter

    @Autowired
    protected lateinit var propertyService: PropertyService

    private val next = ThreadLocal<Boolean>()

    open fun getPluginName(): String {
        return this::class.simpleName ?: "unmapped"
    }

    //todo move this to AbstractEvent
    operator fun set(name: String, value: String) {
        log.info("set plugin prop: {}", name)
        propertyService["plugin.msgCommand.${getPluginName()}.${name.trim().removePrefix(".")}"] = value
    }

    operator fun get(name: String): String? {
        log.info("get plugin prop: {}", name)
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
            val res = s.removePrefix(it)
            if (res.length == s.length) {
                s = res
            } else
                return res.trimStart()
        }
        return s.trimStart()
    }

    fun isAdmin(id: Long): Boolean {
        return id.toString() == propertyService[AbstractPlugin.ADMIN_ACCOUNT_KEY]
    }

    suspend fun <R> doIfAdmin(id: Long, invoke: suspend () -> R): R? {
        if (isAdmin(id)) {
            return invoke()
        }
        return null
    }
}
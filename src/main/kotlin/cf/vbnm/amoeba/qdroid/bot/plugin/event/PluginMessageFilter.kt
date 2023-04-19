package cf.vbnm.amoeba.qdroid.bot.plugin.event

import cf.vbnm.amoeba.qdroid.bot.QBot
import cf.vbnm.amoeba.qdroid.cq.events.Message
import org.springframework.stereotype.Component
import java.util.*

@Component
class PluginMessageFilter {
    private val eventChain = LinkedList<PluginMessageInterceptor>()
    fun addInterceptor(pluginMessageInterceptor: PluginMessageInterceptor) {
        eventChain.add(pluginMessageInterceptor)
    }

    /**
     * @return 消息是否被消费
     * */
    suspend fun consumeMessage(message: Message, bot: QBot): Boolean {
        eventChain.filter { message.userId == it.receiveUserId }
            .forEach {
                it.handleMessage(message, bot)
                return true
            }
        return false
    }

    fun removeInterceptor(pluginMessageInterceptor: PluginMessageInterceptor) {
        eventChain.remove(pluginMessageInterceptor)
    }
}
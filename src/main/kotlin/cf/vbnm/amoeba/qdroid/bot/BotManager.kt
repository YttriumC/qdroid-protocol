package cf.vbnm.amoeba.qdroid.bot

import cf.vbnm.amoeba.qdroid.bot.plugin.PluginManager
import cf.vbnm.amoeba.qdroid.cq.events.BasePostEvent
import com.fasterxml.jackson.databind.ObjectMapper
import org.quartz.Scheduler
import org.springframework.stereotype.Component
import org.springframework.web.socket.WebSocketSession

@Component
class BotManager(
    private val objectMapper: ObjectMapper,
    private val scheduler: Scheduler,
    private val pluginManager: PluginManager
) :
    HashMap<Long, QBot>() {

    fun handleMessages(session: WebSocketSession, map: Map<String, Any?>) {
        if (BasePostEvent.isPostEvent(map)) {
            val postEvent = BasePostEvent.parseEvent(map, objectMapper)
            if (postEvent.isMetaEvent()) {
                val metaEvent = postEvent.toMetaEvent()
                val lifecycle = metaEvent.takeIf { it.isLifecycle() }?.toLifecycle() ?: return
                var qBot = get(lifecycle.selfId)
                if (qBot == null) {
                    qBot = QBot(lifecycle.selfId, objectMapper, scheduler, pluginManager)
                }
                put(lifecycle.selfId, qBot)
                qBot.setWebSocketSession(session)
                return
            }
            this[postEvent.selfId]?.handleEvent(postEvent)
        } else {
            get((map["echo"] as String).split(':')[0].toLong())?.let {
                it.setWebSocketSession(session)
                it.handleApi(map)
            }
        }
    }
}
package cf.vbnm.amoeba.qdroid.bot

import cf.vbnm.amoeba.core.log.Slf4kt
import cf.vbnm.amoeba.qdroid.bot.plugin.EventPluginManager
import cf.vbnm.amoeba.qdroid.cq.events.BasePostEvent
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Component
import org.springframework.web.socket.WebSocketSession

private val log = Slf4kt.getLogger(BotManager::class.java)

@Component
class BotManager(
    private val objectMapper: ObjectMapper,
    private val eventPluginManager: EventPluginManager
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
                    qBot = QBot(lifecycle.selfId, objectMapper, eventPluginManager)
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
            } ?: log.info("Non-mapped message: {}", map)
        }
    }
}
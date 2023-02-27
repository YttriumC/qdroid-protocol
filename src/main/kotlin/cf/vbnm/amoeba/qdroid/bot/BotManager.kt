package cf.vbnm.amoeba.qdroid.bot

import cf.vbnm.amoeba.qdroid.bot.plugin.PluginManager
import cf.vbnm.amoeba.qdroid.cq.events.BasePostEvent
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Component
import org.springframework.web.socket.WebSocketSession

@Component
class BotManager(private val objectMapper: ObjectMapper,private val pluginManager: PluginManager) :
    HashMap<Long, QBot>() {

    fun handleMessages(session: WebSocketSession, map: Map<String, Any?>) {
        if (BasePostEvent.isPostEvent(map)) {
            val postEvent = BasePostEvent.parseEvent(map, objectMapper)
            if (postEvent.isMetaEvent()) {
                val metaEvent = postEvent.toMetaEvent()
                val lifecycle = metaEvent.takeIf { it.isLifecycle() }!!.toLifecycle()
                var qBot = get(lifecycle.selfId)
                if (qBot == null) {
                    qBot = QBot(lifecycle.selfId, objectMapper)
                }
                put(lifecycle.selfId, qBot)
                qBot.setWebSocketSession(session)
                return
            }
            pluginManager.handleEvent(postEvent,get(postEvent.selfId)!!)
        } else {
            get((map["echo"] as String).split(':')[0].toLong())?.let {
                it.setWebSocketSession(session)
                it.handleApi(map)
            }
        }
    }
}
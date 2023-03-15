package cf.vbnm.amoeba.qdroid.controller.websocket

import cf.vbnm.amoeba.core.log.Slf4kt
import cf.vbnm.amoeba.qdroid.bot.BotManager
import cf.vbnm.amoeba.qdroid.cq.events.BasePostEvent
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Component
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.TextWebSocketHandler


private val log = Slf4kt.getLogger(QDroidWebSocketHandler::class.java)

@Component
class QDroidWebSocketHandler(private val objectMapper: ObjectMapper, private val botManager: BotManager) :
    TextWebSocketHandler() {
    override fun handleTransportError(session: WebSocketSession, exception: Throwable) {
        super.handleTransportError(session, exception)
    }

    override fun afterConnectionClosed(session: WebSocketSession, status: CloseStatus) {
        log.warn("Connect closed: {}", session)
    }

    override fun afterConnectionEstablished(session: WebSocketSession) {
        log.info("Connect Established: {}", session)
    }

    override fun handleTextMessage(session: WebSocketSession, message: TextMessage) {
        try {
            val map = objectMapper.readValue(
                message.payload,
                object : TypeReference<Map<String, Any?>>() {}
            )

            botManager.handleMessages(session, map)
            if (BasePostEvent.isPostEvent(map)) {
                val event = BasePostEvent.parseEvent(map, objectMapper)
                if (!event.isMetaEvent())
                    log.debug("Received event: {}", event)
            }
        } catch (e: Exception) {
            log.warn("Handle message error", e)
        }
    }

}
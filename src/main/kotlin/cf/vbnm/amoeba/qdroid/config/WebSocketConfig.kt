package cf.vbnm.amoeba.qdroid.config

import cf.vbnm.amoeba.qdroid.controller.websocket.QDroidHandshakeInterceptor
import cf.vbnm.amoeba.qdroid.controller.websocket.QDroidWebSocketHandler
import org.springframework.context.annotation.Configuration
import org.springframework.web.socket.config.annotation.EnableWebSocket
import org.springframework.web.socket.config.annotation.WebSocketConfigurer
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry


@Configuration
@EnableWebSocket
open class WebSocketConfig(
    private val chatHandler: QDroidWebSocketHandler,
    private val chatInterceptor: QDroidHandshakeInterceptor
) : WebSocketConfigurer {
    override fun registerWebSocketHandlers(registry: WebSocketHandlerRegistry) {
        registry.addHandler(chatHandler, "/qdroid").apply {
            addInterceptors(chatInterceptor)
            setAllowedOriginPatterns("*")
        }
    }

}
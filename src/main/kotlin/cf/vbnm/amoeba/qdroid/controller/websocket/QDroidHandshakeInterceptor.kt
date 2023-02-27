package cf.vbnm.amoeba.qdroid.controller.websocket

import org.springframework.http.server.ServerHttpRequest
import org.springframework.http.server.ServerHttpResponse
import org.springframework.stereotype.Component
import org.springframework.web.socket.WebSocketHandler
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor
import java.lang.Exception

@Component
class QDroidHandshakeInterceptor: HttpSessionHandshakeInterceptor(){
    override fun afterHandshake(
        request: ServerHttpRequest,
        response: ServerHttpResponse,
        wsHandler: WebSocketHandler,
        ex: Exception?
    ) {
        super.afterHandshake(request, response, wsHandler, ex)
    }
}
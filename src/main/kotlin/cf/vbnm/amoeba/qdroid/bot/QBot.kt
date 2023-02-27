package cf.vbnm.amoeba.qdroid.bot

import cf.vbnm.amoeba.qdroid.cq.api.enums.Action
import cf.vbnm.amoeba.qdroid.cq.events.BasePostEvent
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import java.util.*
import java.util.concurrent.ConcurrentHashMap


class QBot(private val selfId: Long, private val objectMapper: ObjectMapper) {
    @Volatile
    private lateinit var webSocketSession: WebSocketSession
    private val waitList = ConcurrentHashMap<Int, Map<String, Any?>>()

    @Volatile
    private var seq = 0

    fun setWebSocketSession(wsSession: WebSocketSession) {
        this.webSocketSession = wsSession

    }

    fun handleEvent(event: BasePostEvent) {

    }

    fun handleApi(map: Map<String, Any?>) {
        map["echo"]?.toString()?.split(':')?.get(1)?.let {
            waitList.put(it.toInt(), map)
        }
    }

    private fun generateEchoMsg(seq: Int): String {
        return "$selfId:${seq}"
    }

    @Synchronized
    private  fun newSeq(): Int {
        return seq++
    }

    fun <R, T : Action<R>> send(action: T, params: Map<String, Any>, wsSession: WebSocketSession): R? {
        val seq = newSeq()
        val sendData = SendData(action, params, generateEchoMsg(seq))
        wsSession.sendMessage(
            TextMessage(objectMapper.writeValueAsString(sendData))
        )
        //等待最多15秒
        for (i in 1..30) {
            Thread.sleep(500)
            if (waitList.contains(seq)) {
                return objectMapper.convertValue(waitList[seq], action.returnType)
            }
        }
        return null
    }

    data class SendData(
        @JsonProperty("action")
        val action: Action<*>,
        @JsonProperty("params")
        val params: Map<String, Any>?,
        @JsonProperty("echo")
        val echo: String?
    )

}
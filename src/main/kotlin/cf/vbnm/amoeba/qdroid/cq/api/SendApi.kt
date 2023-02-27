package cf.vbnm.amoeba.qdroid.cq.api

import cf.vbnm.amoeba.qdroid.bot.QBot
import cf.vbnm.amoeba.qdroid.cq.api.enums.Action
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import java.util.*


class SendApi(private val objectMapper: ObjectMapper) {
    private val msgQueue = LinkedList<Int>()

}
package cf.vbnm.amoeba.qdroid.bot.plugin.event.msgcmd

import cf.vbnm.amoeba.qdroid.bot.QBot
import cf.vbnm.amoeba.qdroid.cq.MessageDetail
import cf.vbnm.amoeba.qdroid.cq.events.Message
import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate

@Component
class DecodeDream(private val restTemplate: RestTemplate) : BaseMessageCommand() {
    override fun getPrefixes(): Array<String> = arrayOf("解梦")
    override fun getPluginName(): String {
        return "dream"
    }

    override suspend fun handle(bot: QBot, msg: Message) {
        val kwd = removePrefix(msg.message.getText())
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_FORM_URLENCODED
        headers.set("Authorization", "APPCODE ${this["appCode"]}")
        val httpEntity = HttpEntity<Unit>(headers)
        val entity = restTemplate.exchange(
            "https://jisudream.market.alicloudapi.com/dream/search?keyword=${kwd}",
            HttpMethod.GET,
            httpEntity,
            Resp::class.java,
            kwd
        )
        entity.body?.let {
            val replace = it.result[0].content.replace("<p>", "").replace("</p>", "\n")
            msg.reply(bot, MessageDetail.oneText(replace))
        }
    }

    data class Resp(
        @JsonProperty("status")
        val status: Int,
        @JsonProperty("msg")
        val msg: String,
        @JsonProperty("result")
        val result: List<Result>
    ) {
        data class Result(
            @JsonProperty("name")
            val name: String,
            @JsonProperty("content")
            val content: String
        )
    }
}
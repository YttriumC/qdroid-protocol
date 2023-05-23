package cf.vbnm.amoeba.qdroid.bot.plugin.event.msgcmd

import cf.vbnm.amoeba.core.CoreProperty
import cf.vbnm.amoeba.qdroid.bot.QBot
import cf.vbnm.amoeba.qdroid.cq.MessageDetail
import cf.vbnm.amoeba.qdroid.cq.events.Message
import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate

@Component
class NewsPlugin(private val restTemplate: RestTemplate, coreProperty: CoreProperty) : BaseMessageCommand() {
    override fun getPrefixes() = arrayOf("news")

    override suspend fun handle(bot: QBot, msg: Message) {
        val s = msg.message.getText().rmPrefix()
        val channel = when (val trim = s.trim()) {
            "新闻", "娱乐", "体育", "财经", "军事", "科技", "手机", "数码", "时尚", "游戏", "教育", "健康", "旅游" -> trim
            "list" -> {
                msg.reply(
                    bot, MessageDetail.oneText(
                        "频道列表: 新闻, 娱乐, 体育, 财经, 军事, 科技, " +
                                "手机, 数码, 时尚, 游戏, 教育, 健康, 旅游\n默认为 '财经'"
                    )
                )
                return
            }

            "" -> "财经"
            else -> {
                msg.reply(
                    bot, MessageDetail.oneText("请选择正确的频道, 使用 'news list' 查看新闻频道, 默认频道为 '财经'")
                )
                return
            }
        }
        val headers = HttpHeaders()
        headers.set("Authorization", "APPCODE ${this["appCode"]}")
        val entity = HttpEntity<Unit>(headers)
        val newsResp = restTemplate.exchange(
            "https://lznews.market.alicloudapi.com/lundroid/news?channel=${channel}&page=1",
            HttpMethod.GET, entity, NewsResp::class.java
        ).body
        val sb = StringBuilder()
        newsResp?.data?.forEach {
            sb + it.title + '\n'
            sb + "链接:" + it.url + '\n'
        }
        msg.reply(bot, MessageDetail.oneText(sb))
    }

    operator fun StringBuilder.plus(content: Any?): StringBuilder {
        return this.append(content)
    }
}


data class NewsResp(
    @JsonProperty("data") val `data`: List<Data>?, @JsonProperty("resp") val resp: Resp
) {
    data class Data(
        @JsonProperty("commentCount") val commentCount: Int,
        @JsonProperty("digest") val digest: String,
        @JsonProperty("docid") val docid: String,
        @JsonProperty("hasImg") val hasImg: Int?,
        @JsonProperty("imgsrc") val imgsrc: String,
        @JsonProperty("imgsrc3gtype") val imgsrc3gtype: String,
        @JsonProperty("liveInfo") val liveInfo: String?,
        @JsonProperty("priority") val priority: Int,
        @JsonProperty("ptime") val ptime: String,
        @JsonProperty("source") val source: String,
        @JsonProperty("stitle") val stitle: String,
        @JsonProperty("title") val title: String,
        @JsonProperty("url") val url: String
    )

    data class Resp(
        @JsonProperty("RespCode") val respCode: Int, @JsonProperty("RespMsg") val respMsg: String
    )
}
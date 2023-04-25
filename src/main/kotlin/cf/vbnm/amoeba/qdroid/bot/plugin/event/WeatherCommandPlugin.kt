package cf.vbnm.amoeba.qdroid.bot.plugin.event

import cf.vbnm.amoeba.qdroid.bot.QBot
import cf.vbnm.amoeba.qdroid.bot.plugin.PluginOrder
import cf.vbnm.amoeba.qdroid.cq.MessageDetail
import cf.vbnm.amoeba.qdroid.cq.code.data.Reply
import cf.vbnm.amoeba.qdroid.cq.code.data.Text
import cf.vbnm.amoeba.qdroid.cq.events.Message
import com.fasterxml.jackson.annotation.JsonProperty
import com.google.common.base.Splitter
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.getForObject

@PluginOrder(100)
@Component
class WeatherCommandPlugin(
    private val restTemplate: RestTemplate
) : MessageCommand() {
    override fun getPrefixes() = arrayOf("天气", "/天气")
    override fun getPluginName(): String = "weather"
    override suspend fun handle(bot: QBot, msg: Message) {
        val list = Splitter.on(' ').omitEmptyStrings().splitToList(msg.message.getText())
        if (list.size < 2) {
            msg.reply(bot, MessageDetail.of(Text("请按照下列格式输入: \r\n天气  北京"), Reply(msg.messageId)))
            return
        }
        val code = this["secretCode"]
        if (code == null) {
            msg.reply(
                bot,
                MessageDetail.of(Text("请设置secretCode, key=plugin.weather.secretCode"), Reply(msg.messageId)),
            )
            return
        }
        val weatherData = restTemplate.getForObject<WeatherData>(
            "https://api.seniverse.com/v3/weather/daily.json?" +
                    "key=$code&location=${list[1]}&language=zh-Hans&unit=c&start=0&days=3"
        )
        val text = StringBuilder()
        if (weatherData.results.isNotEmpty()) {
            text.append("位置: ${weatherData.results[0].location.name}\r\n")
            weatherData.results[0].daily.forEach {
                text.append("日期: ")
                text.append(it.date)
                text.append("\r\n")
                text.append("白天: ${it.textDay}, 晚间: ${it.textNight}")
                text.append("\r\n")
                text.append("最高温度: ${it.high}℃,最低温度: ${it.low}℃")
                text.append("\r\n")
                text.append("相对湿度: ${it.humidity}%")
                text.append("\r\n")
            }
            text.append("数据更新时间: ${weatherData.results[0].lastUpdate}")
        } else {
            text.append("未查询到相关信息")
        }
        msg.reply(bot, MessageDetail.of(Text(text.toString()), Reply(msg.messageId)))
    }

}

data class WeatherData(
    @JsonProperty("results")
    val results: List<Result>
) {
    data class Result(
        @JsonProperty("daily")
        val daily: List<Daily>,
        @JsonProperty("last_update")
        val lastUpdate: String,
        @JsonProperty("location")
        val location: Location
    ) {
        data class Daily(
            @JsonProperty("code_day")
            val codeDay: String,
            @JsonProperty("code_night")
            val codeNight: String,
            @JsonProperty("date")
            val date: String,
            @JsonProperty("high")
            val high: String,
            @JsonProperty("humidity")
            val humidity: String,
            @JsonProperty("low")
            val low: String,
            @JsonProperty("precip")
            val precip: String,
            @JsonProperty("rainfall")
            val rainfall: String,
            @JsonProperty("text_day")
            val textDay: String,
            @JsonProperty("text_night")
            val textNight: String,
            @JsonProperty("wind_direction")
            val windDirection: String,
            @JsonProperty("wind_direction_degree")
            val windDirectionDegree: String,
            @JsonProperty("wind_scale")
            val windScale: String,
            @JsonProperty("wind_speed")
            val windSpeed: String
        )

        data class Location(
            @JsonProperty("country")
            val country: String,
            @JsonProperty("id")
            val id: String,
            @JsonProperty("name")
            val name: String,
            @JsonProperty("path")
            val path: String,
            @JsonProperty("timezone")
            val timezone: String,
            @JsonProperty("timezone_offset")
            val timezoneOffset: String
        )
    }
}
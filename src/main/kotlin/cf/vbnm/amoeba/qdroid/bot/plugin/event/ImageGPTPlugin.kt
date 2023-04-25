package cf.vbnm.amoeba.qdroid.bot.plugin.event

import cf.vbnm.amoeba.qdroid.bot.QBot
import cf.vbnm.amoeba.qdroid.cq.MessageDetail
import cf.vbnm.amoeba.qdroid.cq.code.data.Image
import cf.vbnm.amoeba.qdroid.cq.events.Message
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate

@Component
class ImageGPTPlugin(private val restTemplate: RestTemplate) : MessageCommand() {
    override fun getPrefixes(): Array<String> = arrayOf("image", "/image")

    override suspend fun handle(bot: QBot, msg: Message) {
        /*val parts = Splitter.on(' ').omitEmptyStrings().splitToList(msg.message.getText())
        if ((parts.size > 2)) {
            ImageSize.parseSize(parts[1])?.let {
                HttpEntity(ImageReq(removePrefix(msg.message.getText()),))
            }
        }*/
        val headers = HttpHeaders()
        headers.setBearerAuth("fk189820-RHya5FVWmapkG0JXPmfyjTB6vpTLedav")
        headers.contentType = MediaType.APPLICATION_JSON
        val httpEntity = HttpEntity(ImageReq(removePrefix(msg.message.getText())), headers)
        val imgResp = restTemplate.exchange(
            "https://openai.api2d.net/v1/images/generations",
            HttpMethod.POST,
            httpEntity,
            ImageResp::class.java
        )

        imgResp.body?.let { resp ->
            resp.data.forEach {

                msg.reply(
                    bot,
                    MessageDetail().addReply(msg.messageId)
                        .addNotReply(Image(it.url, threads = 1))
                )
            }
        }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    data class ImageReq(
        @field:JsonProperty("prompt")
        val prompt: String,
        @field:JsonProperty("n")
        val n: Int = 1,
        @field:JsonProperty("size")
        val size: ImageSize = ImageSize.SIZE_512,
        @field:JsonProperty("response_format")
        val responseFormat: String = "url",
        @field:JsonProperty("user")
        val user: String? = null
    )

    data class ImageResp(
        @JsonProperty("created") val created: Int, @JsonProperty("data") val data: ArrayList<Data>
    ) {
        data class Data(@JsonProperty("url") val url: String)
    }

    @JsonSerialize(using = ImageSize.Serializer::class)
    enum class ImageSize(val size: String, val sizeName: String) {
        SIZE_256("256x256", "small"),
        SIZE_512("512x512", "meddle"),
        SIZE_1024("1024x1024", "large");

        class Serializer : JsonSerializer<ImageSize>() {
            override fun serialize(value: ImageSize, gen: JsonGenerator, serializers: SerializerProvider?) {
                gen.writeString(value.size)
            }

        }

        companion object {
            fun parseSize(size: String): ImageSize? {
                return values().firstOrNull { size == it.sizeName }
            }

            fun parseSizeOrDefault(size: String, default: ImageSize): ImageSize {
                return values().firstOrNull { size == it.sizeName } ?: default
            }
        }
    }
}
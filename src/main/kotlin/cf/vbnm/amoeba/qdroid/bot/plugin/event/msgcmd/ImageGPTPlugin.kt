package cf.vbnm.amoeba.qdroid.bot.plugin.event.msgcmd

import cf.vbnm.amoeba.core.log.Slf4kt
import cf.vbnm.amoeba.qdroid.bot.QBot
import cf.vbnm.amoeba.qdroid.cq.MessageDetail
import cf.vbnm.amoeba.qdroid.cq.code.data.Image
import cf.vbnm.amoeba.qdroid.cq.events.Message
import cf.vbnm.chatgpt.client.ChatGPTClient
import org.springframework.stereotype.Component

private val log = Slf4kt.getLogger(ImageGPTPlugin::class.java)

@Component
class ImageGPTPlugin(private val chatGPT: ChatGPTClient) : BaseMessageCommand() {
    override fun getPrefixes(): Array<String> = arrayOf("image", "/image")
    override fun getPluginName(): String = "chatGPT"

    override suspend fun handle(bot: QBot, msg: Message) {
        if (!isPluginEnabled()) {
            msg.reply(bot, MessageDetail.oneText("该插件已关闭"))
            return
        }
        val imageResp = chatGPT.generationImage(removePrefix(msg.message.getText()))
        imageResp.data.forEach {
            log.info("Generated image: {}", it.url)
            val file = bot.downloadFile(it.url, threadCount = 1)
            msg.reply(
                bot,
                MessageDetail().addReply(msg.messageId).addNotReply(Image("file:///${file.data.filePath}", threads = 1))
            )
        }
    }
}
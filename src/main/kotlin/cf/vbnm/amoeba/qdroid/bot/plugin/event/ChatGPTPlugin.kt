package cf.vbnm.amoeba.qdroid.bot.plugin.event

import cf.vbnm.amoeba.core.log.Slf4kt
import cf.vbnm.amoeba.qdroid.bot.QBot
import cf.vbnm.amoeba.qdroid.cq.MessageDetail
import cf.vbnm.amoeba.qdroid.cq.api.data.GetMsg
import cf.vbnm.amoeba.qdroid.cq.events.Message
import cf.vbnm.chatgpt.client.ChatGPT
import cf.vbnm.chatgpt.entity.chat.ChatCompletion
import cf.vbnm.chatgpt.listener.StreamListener
import com.fasterxml.jackson.databind.ObjectMapper
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.springframework.stereotype.Component

private val log = Slf4kt.getLogger(ChatGPTPlugin::class.java)

@Component
class ChatGPTPlugin(
    private val chatGPT: ChatGPT,
    private val objectMapper: ObjectMapper
) : MessageCommand() {
    override fun getPrefixes(): Array<String> = arrayOf("/ask", "ask")


    override suspend fun handle(bot: QBot, msg: Message) {
        var text = msg.message.getText()
        text = removePrefix(text)
        var replyTo: GetMsg? = null
        msg.message.getReply()?.let { reply ->
            replyTo = bot.getMsg(reply.data.id.toLong())
            replyTo?.data?.let {
                text = it.message.getText()
            }
        } ?: log.info("查询GPT: {}", text)
        val listener = object : StreamListener(objectMapper) {
            override fun onMsg(message: String) {
                print(message)
            }

            override fun onComplete(message: String) {
                println()
                val reply = MessageDetail.oneText(message.replace("\n", "\r\n"))
                    .addReply(replyTo?.data?.messageId ?: msg.messageId)
                @OptIn(DelicateCoroutinesApi::class)
                GlobalScope.launch {
                    msg.reply(bot, reply)
                }
            }
        }
        val message = cf.vbnm.chatgpt.entity.chat.Message.of(text)
        val chatCompletion = ChatCompletion.builder()
            .messages(arrayListOf(message).apply {
                if (replyTo != null) {
                    val s = removePrefix(msg.message.getText())
                    if (s.isNotBlank())
                        add(0, cf.vbnm.chatgpt.entity.chat.Message.ofSystem(s))
                }
            }).build()
        chatGPT.streamChatCompletion(chatCompletion, listener)
    }
}

package cf.vbnm.amoeba.qdroid.bot.plugin.event

import cf.vbnm.amoeba.core.CoreProperty
import cf.vbnm.amoeba.core.log.Slf4kt
import cf.vbnm.amoeba.qdroid.bot.QBot
import cf.vbnm.amoeba.qdroid.cq.MessageDetail
import cf.vbnm.amoeba.qdroid.cq.events.Message
import cf.vbnm.amoeba.qdroid.cq.events.enums.MessagePartialType
import cf.vbnm.chatgpt.client.ChatGPT
import cf.vbnm.chatgpt.entity.chat.ChatCompletion
import cf.vbnm.chatgpt.listener.StreamListener
import com.fasterxml.jackson.databind.ObjectMapper
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.quartz.Scheduler
import org.springframework.stereotype.Component

private val log = Slf4kt.getLogger(GPTPlugin::class.java)

@Component
class GPTPlugin(
    coreProperty: CoreProperty,
    scheduler: Scheduler,
    pluginMessageFilter: PluginMessageFilter,
    private val chatGPT: ChatGPT,
    private val objectMapper: ObjectMapper
) :
    MessageCommandPlugin(
        coreProperty, scheduler,
        pluginMessageFilter
    ) {
    override fun getPrefixes(): Array<String> = arrayOf("/ask", "ask")


    override suspend fun handle(bot: QBot, event: Message) {
        var text = event.message.getText()
        text = removePrefix(text)
        log.info("查询GPT: {}", text)
        val listener = object : StreamListener(objectMapper) {
            override fun onMsg(message: String) {
                print(message)
            }

            override fun onComplete(message: String) {
                println()
                val reply = MessageDetail.oneText(message.replace("\n", "\r\n"))
                reply.add(
                    MessageDetail.MessagePartial(
                        MessagePartialType.REPLY, mapOf(
                            Pair("id", event.messageId)
                        )
                    )
                )
                @OptIn(DelicateCoroutinesApi::class)
                GlobalScope.launch {
                    event.reply(bot, reply)
                }
            }
        }
        val message = cf.vbnm.chatgpt.entity.chat.Message.of(text)
        val chatCompletion = ChatCompletion.builder()
            .messages(listOf(message))
            .build()
        chatGPT.streamChatCompletion(chatCompletion, listener)
    }
}

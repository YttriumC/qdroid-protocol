package cf.vbnm.amoeba.qdroid.bot.plugin.event.msgcmd

import cf.vbnm.amoeba.core.log.Slf4kt
import cf.vbnm.amoeba.qdroid.bot.QBot
import cf.vbnm.amoeba.qdroid.cq.MessageDetail
import cf.vbnm.amoeba.qdroid.cq.events.Message
import cf.vbnm.chatgpt.client.ChatGPTClient
import cf.vbnm.chatgpt.entity.chat.ChatCompletion
import cf.vbnm.chatgpt.entity.chat.ChatMessage
import cf.vbnm.chatgpt.listener.StreamListener
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.annotation.PostConstruct
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.springframework.stereotype.Component
import java.util.function.Supplier

private val log = Slf4kt.getLogger(ChatGPTPlugin::class.java)

@Component
class ChatGPTPlugin(
    private val chatGPT: ChatGPTClient,
    private val objectMapper: ObjectMapper
) : BaseMessageCommand() {
    private var contentSuffix = "\n  ——AIGC powered by qdroid"
    override fun getPrefixes(): Array<String> = arrayOf("/ask", "ask")

    override fun getPluginName(): String = "chatGPT"

    @PostConstruct
    fun init() {
        this["suffix"]?.let {
            contentSuffix = it
        }
    }

    override suspend fun handle(bot: QBot, msg: Message) {
        val listener = object : StreamListener(objectMapper) {
            override fun onMsg(message: String) {
                print(message)
            }

            override fun onComplete(message: String) {
                println()
                @OptIn(DelicateCoroutinesApi::class)
                GlobalScope.launch {
                    if (message.length > 3413) {
                        (message + contentSuffix).chunkedSequence(3301).forEach {
                            val reply = MessageDetail.oneText(it)
                            if (removePrefix(msg.message.getText()).trim()
                                    .isEmpty() && msg.message.getReply() != null
                            ) {
                                msg.message.getReply()?.data?.id?.let { id -> reply.addReply(id) }
                            }
                            msg.reply(bot, reply)
                        }
                    } else {
                        val reply = MessageDetail.oneText(
                            message.replace("\r\n", "\n")
                                .replace('\r', '\n') + contentSuffix
                        )
                        if (removePrefix(msg.message.getText()).trim().isEmpty() && msg.message.getReply() != null) {
                            msg.message.getReply()?.data?.id?.let { id -> reply.addReply(id) }
                        }
                        msg.reply(bot, reply)
                    }
                }
            }
        }
        val replies = getReplies(bot, msg.messageId)
        replies.add(
            0,
            ChatMessage.ofSystem(
                "You are qdroid, a large language model offered by https://github.com/Lu7fer . " +
                        "Follow the user's instructions carefully. Respond using markdown."
            )
        )
        log.info("查询GPT: {}", replies)
        val chatCompletion = ChatCompletion().chatMessages(replies)
        chatCompletion.temperature(1.0)
        chatGPT.streamChatCompletion(chatCompletion, listener)
    }

    @PostConstruct
    fun getKey() {
        GPTKeySupplier.plugin = this
    }

    private suspend fun getReplies(bot: QBot, startMsgId: Int): ArrayList<ChatMessage> {
        val maxWords = 2048
        val me = bot.selfId
        var msgDetail = bot.getMsg(startMsgId)
        val msgList = ArrayList<ChatMessage>()
        var words = 0
        var dive = 0
        val maxDive = 16
        while (dive++ < maxDive) {
            val message = if (msgDetail.data.sender.userId == me) {
                ChatMessage.ofAssistant(
                    msgDetail.data.message.getText().trim().removeSuffix(contentSuffix.trim()).trim()
                )
            } else {
                ChatMessage.ofUser(removePrefix(msgDetail.data.message.getText().trim()).trim())
            }
            val reply = msgDetail.data.message.getReply()
            if (message.content.isNotBlank()) {
                if ((words + message.content.length) > maxWords && msgList.size > 0) {
                    break
                } else {
                    words += message.content.length
                }
                msgList.add(0, message)
            }
            if (reply == null) {
                break
            }
            msgDetail = bot.getMsg(reply.data.id)
        }
        return msgList
    }

    class GPTKeySupplier : Supplier<String> {
        companion object {
            lateinit var plugin: ChatGPTPlugin
        }

        override fun get(): String {
            try {
                return plugin["key"]!!
            } catch (e: Exception) {
                throw IllegalArgumentException("The chatgpt key has not set yet.", e)
            }
        }

    }
}

package cf.vbnm.amoeba.qdroid.bot.plugin.event.msgcmd

import cf.vbnm.amoeba.core.log.Slf4kt
import cf.vbnm.amoeba.qdroid.bot.QBot
import cf.vbnm.amoeba.qdroid.bot.plugin.AbstractPlugin.Companion.ADMIN_ACCOUNT_KEY
import cf.vbnm.amoeba.qdroid.cq.MessageDetail
import cf.vbnm.amoeba.qdroid.cq.events.Message
import jakarta.annotation.PostConstruct
import org.springframework.stereotype.Component
import java.util.*

private val log = Slf4kt.getLogger(AuthPlugin::class.java)

@Component
class AuthPlugin : BaseMessageCommand() {
    override fun getPrefixes(): Array<String> = arrayOf("auth", "/auth")
    private var authToken: UUID? = null

    @PostConstruct
    fun generateAuthToken() {
        if (propertyService[ADMIN_ACCOUNT_KEY] != null) {
            return
        }
        authToken = UUID.randomUUID()
        log.warn("Generated Auth Token: '{}'", authToken)
    }

    override suspend fun handle(bot: QBot, msg: Message) {
        msg.doIfPrivateMessage {
            when (val content = removePrefix(msg.message.getText()).trim()) {
                "delete" -> {
                    propertyService.delete(ADMIN_ACCOUNT_KEY)
                    generateAuthToken()
                    msg.reply(bot, MessageDetail.oneText("删除认证成功, 新的认证token为: $authToken"))
                }

                else -> {
                    if (authToken != null) {
                        if (authToken.toString() == content) {
                            propertyService[ADMIN_ACCOUNT_KEY] = it.userId.toString()
                            msg.reply(bot, MessageDetail.oneText("认证成功"))
                        } else {
                            msg.reply(bot, MessageDetail.oneText("认证失败"))
                        }
                    } else {
                        msg.reply(bot, MessageDetail.oneText("存在已认证账号"))
                        log.info("Authenticated account already exist: {}", propertyService[ADMIN_ACCOUNT_KEY])
                    }
                }
            }
        }
    }

}
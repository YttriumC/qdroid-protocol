package cf.vbnm.amoeba.qdroid.bot.plugin.event.msgcmd

import cf.vbnm.amoeba.qdroid.bot.QBot
import cf.vbnm.amoeba.qdroid.cq.events.Message
import org.springframework.stereotype.Component

@Component
class RecallPlugin : BaseMessageCommand() {
    override fun getPrefixes(): Array<String> = arrayOf("recall", "/recall")

    override suspend fun handle(bot: QBot, msg: Message) {
        doIfAdmin(msg.userId) {
            msg.message.let { md ->
                md.getReply()?.let { reply ->
                    if (bot.getMsg(reply.data.id).data.sender.userId == bot.selfId) {
                        bot.deleteMsg(reply.data.id)
                        msg.doIfGroupMessage { gm ->
                            if (bot.getGroupMemberInfo(gm.groupId, bot.selfId, true).data.role != "member") {
                                if (bot.getGroupMemberInfo(gm.groupId, msg.userId).data.role != "owner") {
                                    bot.deleteMsg(msg.messageId)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
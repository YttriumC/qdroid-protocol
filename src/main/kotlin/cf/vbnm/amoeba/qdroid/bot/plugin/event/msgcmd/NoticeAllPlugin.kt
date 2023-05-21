package cf.vbnm.amoeba.qdroid.bot.plugin.event.msgcmd

import cf.vbnm.amoeba.qdroid.bot.QBot
import cf.vbnm.amoeba.qdroid.cq.MessageDetail
import cf.vbnm.amoeba.qdroid.cq.events.Message
import org.springframework.stereotype.Component

@Component
class NoticeAllPlugin : BaseMessageCommand() {
    override fun getPrefixes(): Array<String> = arrayOf("noticeAll")

    override suspend fun handle(bot: QBot, msg: Message) {
        msg.doIfPrivateMessage {
            val notice = removePrefix(msg.message.getText())
            if (isAdmin(msg.userId)) {
                bot.getFriendList().data.forEach {
                    bot.sendPrivateMsg(it.userId, message = MessageDetail.oneText(notice))
                }
                bot.getGroupList().data.forEach {
                    bot.sendGroupMsg(it.groupId, message = MessageDetail.oneText(notice))
                }
                msg.reply(bot, MessageDetail.oneText("Success"))
            }
        }
    }
}
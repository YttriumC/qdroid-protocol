package cf.vbnm.amoeba.qdroid.bot.plugin.event.msgcmd

import cf.vbnm.amoeba.qdroid.bot.QBot
import cf.vbnm.amoeba.qdroid.cq.MessageDetail
import cf.vbnm.amoeba.qdroid.cq.events.Message
import com.google.common.base.Splitter
import org.springframework.stereotype.Component

@Component
class DeletePropertyPlugin : BaseMessageCommand() {
    override fun getPrefixes() = arrayOf("/delete")

    override suspend fun handle(bot: QBot, msg: Message) {
        val splits = Splitter.on(' ').omitEmptyStrings().splitToList(msg.message.getText())
        if (splits.size < 2) {
            msg.reply(bot, MessageDetail().apply {
                addText("使用格式: \r\n/delete 属性名")
            })
        }
        msg.reply(bot, MessageDetail.oneText("删除: ${splits[1]}=${propertyService[splits[1]]}"))
        propertyService.delete(splits[1])
    }
}
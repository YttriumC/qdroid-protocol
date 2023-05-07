package cf.vbnm.amoeba.qdroid.bot.plugin.event.msgcmd

import cf.vbnm.amoeba.qdroid.bot.QBot
import cf.vbnm.amoeba.qdroid.cq.MessageDetail
import cf.vbnm.amoeba.qdroid.cq.events.Message
import com.google.common.base.Splitter
import org.springframework.stereotype.Component

@Component
class SetPropertyPlugin : BaseMessageCommand() {
    override fun getPrefixes() = arrayOf("/set")

    override suspend fun handle(bot: QBot, msg: Message) {
        val splits = Splitter.on(' ').omitEmptyStrings().limit(3).splitToList(msg.message.getText())
        if (splits.size < 3) {
            msg.reply(bot, MessageDetail().apply {
                addText("使用格式: \r\n/set 属性名 属性值")
            })
        }
        propertyService.set(splits[1], splits[2]).let {
            if (it == null) {
                msg.reply(bot, MessageDetail.oneText("保存失败"))
            } else
                msg.reply(bot, MessageDetail.oneText("保存成功"))
        }

    }
}
package cf.vbnm.amoeba.qdroid.bot.plugin.event

import cf.vbnm.amoeba.qdroid.bot.QBot
import cf.vbnm.amoeba.qdroid.cq.MessageDetail
import cf.vbnm.amoeba.qdroid.cq.events.Message
import cf.vbnm.amoeba.web.service.PropertyService
import com.google.common.base.Splitter
import org.quartz.Scheduler
import org.springframework.stereotype.Component

@Component
class SetPropertyPlugin(
    override val coreProperty: PropertyService,
    scheduler: Scheduler,
    pluginMessageFilter: PluginMessageFilter
) :
    MessageCommandPlugin(
        coreProperty,
        scheduler, pluginMessageFilter
    ) {
    override fun getPrefixes() = arrayOf("/set")

    override suspend fun handle(bot: QBot, event: Message) {
        val splits = Splitter.on(' ').omitEmptyStrings().splitToList(event.message.getText())
        if (splits.size < 3) {
            event.reply(bot, MessageDetail().apply {
                addText("使用格式: \r\n/set 属性名 属性值")
            })
        }
        coreProperty.set(splits[1], splits[2]).let {
            if (it == null) {
                event.reply(bot, MessageDetail.oneText("保存失败"))
            } else
                event.reply(bot, MessageDetail.oneText("保存成功"))
        }

    }
}
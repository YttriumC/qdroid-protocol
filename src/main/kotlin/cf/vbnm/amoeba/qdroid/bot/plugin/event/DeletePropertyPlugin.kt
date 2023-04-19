package cf.vbnm.amoeba.qdroid.bot.plugin.event

import cf.vbnm.amoeba.qdroid.bot.QBot
import cf.vbnm.amoeba.qdroid.cq.MessageDetail
import cf.vbnm.amoeba.qdroid.cq.events.Message
import cf.vbnm.amoeba.web.service.PropertyService
import com.google.common.base.Splitter
import org.quartz.Scheduler
import org.springframework.stereotype.Component

@Component
class DeletePropertyPlugin(
    override val coreProperty: PropertyService,
    scheduler: Scheduler,
    pluginMessageFilter: PluginMessageFilter
) :
    MessageCommandPlugin(
        coreProperty,
        scheduler, pluginMessageFilter
    ) {
    override fun getPrefixes() = arrayOf("/delete")

    override suspend fun handle(bot: QBot, event: Message) {
        val splits = Splitter.on(' ').omitEmptyStrings().splitToList(event.message.getText())
        if (splits.size < 2) {
            event.reply(bot, MessageDetail().apply {
                addText("使用格式: \r\n/delete 属性名")
            })
        }
        event.reply(bot, MessageDetail.oneText("删除: ${splits[1]}=${coreProperty[splits[1]]}"))
        coreProperty.delete(splits[1])
    }
}
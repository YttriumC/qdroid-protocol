package cf.vbnm.amoeba.qdroid.bot.plugin.event.msgcmd

import cf.vbnm.amoeba.qdroid.bot.QBot
import cf.vbnm.amoeba.qdroid.bot.statistics.Statistics
import cf.vbnm.amoeba.qdroid.cq.MessageDetail
import cf.vbnm.amoeba.qdroid.cq.events.Message
import org.springframework.stereotype.Component

@Component
class StatusPlugin : BaseMessageCommand() {
    override fun getPrefixes(): Array<String> = arrayOf("/stat", "stat")

    override suspend fun handle(bot: QBot, msg: Message) {

        if (removePrefix(msg.message.getText()).trim() == "global") {
            msg.reply(
                bot, MessageDetail.oneText(
                    """
            状态统计
            发送Api: ${Statistics.global.totalSentApi},成功Api: ${Statistics.global.totalSuccessApi},
            失败Api: ${Statistics.global.totalFailedApi},丢弃Api: ${Statistics.global.totalDroppedApi}
            收到Event: ${Statistics.global.totalReceivedEvent}, {消息: ${Statistics.global.totalReceivedMsgEvent}, 
            通知: ${Statistics.global.totalReceivedNoticeEvent}, 请求: ${Statistics.global.totalReceivedRequestEvent}, 元事件: ${Statistics.global.totalReceivedMetaEvent}}
        """.trimIndent()
                )
            )
        } else {
            msg.reply(
                bot, MessageDetail.oneText(
                    """
            状态统计
            当前账号: ${bot.selfId}
            发送Api: ${bot.statistics.totalSentApi},成功Api: ${bot.statistics.totalSuccessApi},失败Api: ${bot.statistics.totalFailedApi}
            收到Event: ${bot.statistics.totalReceivedEvent}, {消息: ${bot.statistics.totalReceivedMsgEvent}, 
            通知: ${bot.statistics.totalReceivedNoticeEvent}, 请求: ${bot.statistics.totalReceivedRequestEvent}, 元事件: ${bot.statistics.totalReceivedMetaEvent}}
        """.trimIndent()
                )
            )
        }

    }
}
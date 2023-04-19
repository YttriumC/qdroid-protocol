package cf.vbnm.amoeba.qdroid.bot.plugin.event

import cf.vbnm.amoeba.qdroid.bot.QBot
import cf.vbnm.amoeba.qdroid.cq.events.Message

/**
 * 对话管理
 * */
abstract class PluginMessageInterceptor(val receiveUserId: Long) {
    abstract suspend fun handleMessage(msg: Message, bot: QBot)

}


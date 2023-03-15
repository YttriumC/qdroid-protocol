package cf.vbnm.amoeba.qdroid.bot.plugin

import cf.vbnm.amoeba.qdroid.bot.QBot
import cf.vbnm.amoeba.qdroid.cq.events.Message

abstract class PluginMessageInterceptor(val receiveUserId: Long) {
    abstract fun handleMessage(msg: Message, bot: QBot)

}


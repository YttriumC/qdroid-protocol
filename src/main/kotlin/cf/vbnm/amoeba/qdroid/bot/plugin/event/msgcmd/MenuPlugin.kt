package cf.vbnm.amoeba.qdroid.bot.plugin.event.msgcmd

import cf.vbnm.amoeba.qdroid.bot.QBot
import cf.vbnm.amoeba.qdroid.cq.MessageDetail
import cf.vbnm.amoeba.qdroid.cq.events.Message
import com.google.common.base.Splitter
import org.springframework.stereotype.Component

@Component
class MenuPlugin(private val menus: List<Menuable>) : BaseMessageCommand() {
    override fun getPrefixes(): Array<String> = arrayOf("菜单", "menu", "/menu")


    override fun getPluginName(): String {
        return "menu"
    }

    override suspend fun handle(bot: QBot, msg: Message) {
        val list = Splitter.on(' ').omitEmptyStrings().splitToList(msg.message.getText())
        val announce = this["announce"].orEmpty()
        when (list.size) {
            1 -> {
                msg.reply(bot, MessageDetail.oneText(menus.run {
                    val builder = StringBuilder()
                    builder.append("选项菜单:").append('\n')
                    forEach {
                        builder.append(it.getName()).append('\n')
                    }
                    builder.append(announce)
                    builder
                }))
            }

            2 -> {
                msg.reply(bot, MessageDetail.oneText(menus.run {
                    val builder = StringBuilder()
                    builder.append("选项菜单:").append('\n')
                    stream().filter { it.getName() == list[1] }.forEach {
                        builder.append(it.getHelp()).append('\n')
                    }
                    builder.append(announce)
                    builder
                }))
            }

            else -> {}
        }

    }
}

interface Menuable {
    fun getName(): String
    fun getHelp(): String
    fun getHelp(vararg instructions: String): String
}
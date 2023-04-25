package cf.vbnm.amoeba.qdroid.bot.plugin.scheduled

import cf.vbnm.amoeba.core.CoreProperty
import cf.vbnm.amoeba.core.log.Slf4kt
import cf.vbnm.amoeba.qdroid.bot.QBot
import org.springframework.stereotype.Component

private val log = Slf4kt.getLogger(GroupSignInPlugin::class.java)

@Component
class GroupSignInPlugin(coreProperty: CoreProperty) : BaseScheduledPlugin(
    coreProperty
) {
    override suspend fun apply(bot: QBot) {
        val groupList = bot.getGroupList()
        groupList.data?.forEach {
            log.info("在{}群组中签到完成", it.groupId)
            bot.sendGroupSign(it.groupId)
        }

    }

    override fun initTrigger() {
        addCronTrigger("5 0 0 * * ?")
//        addCronTrigger("5 54 0 * * ?")
    }
}
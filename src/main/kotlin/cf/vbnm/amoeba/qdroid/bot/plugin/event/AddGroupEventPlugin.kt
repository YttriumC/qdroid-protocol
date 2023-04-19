package cf.vbnm.amoeba.qdroid.bot.plugin.event

import cf.vbnm.amoeba.core.CoreProperty
import cf.vbnm.amoeba.qdroid.bot.QBot
import cf.vbnm.amoeba.qdroid.bot.job.TimeoutJob
import cf.vbnm.amoeba.qdroid.const.Constant
import cf.vbnm.amoeba.qdroid.cq.MessageDetail
import cf.vbnm.amoeba.qdroid.cq.events.Message
import cf.vbnm.amoeba.qdroid.cq.events.request.GroupRequest
import org.quartz.Scheduler
import org.springframework.stereotype.Component
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.reflect.KClass

@Component
class AddGroupEventPlugin(
    coreProperty: CoreProperty,
    scheduler: Scheduler,
    pluginMessageFilter: PluginMessageFilter
) : BaseEventPlugin<GroupRequest>(
    coreProperty, scheduler, pluginMessageFilter
) {

    override suspend fun apply(bot: QBot, event: GroupRequest) {
        if (event.subType == "invite") {
            bot.setGroupAddRequest(event.flag, event.subType)
            abortFilter()
            return
        }
        coreProperty.getWildcardProperty(event.run {
            val stringBuilder = StringBuilder()
            stringBuilder.append(Constant.GROUP_BLOCK_PREFIX)
            stringBuilder.append(event.groupId).append(".*")
            stringBuilder.toString()
        }).filter { it.name.endsWith(event.userId.toString()) }.forEach {
            bot.setGroupAddRequest(
                event.flag,
                event.subType,
                false,
                "Sorry, 你的加群请求被拒绝了, 原因是: ${it.value}"
            )
        }
        var isApproved = false
        if (coreProperty[Constant.PERMIT_REQUEST_PREFIX + event.groupId] != "") {
            bot.setGroupAddRequest(event.flag, event.subType, true)
            if (coreProperty["${Constant.CAPTCHA_ENABLE_PREFIX}${event.groupId}"] != "") {
                bot.setGroupBan(event.groupId, event.userId, 5 * 60)
                val captcha = generateCaptcha()
                bot.sendPrivateMsg(
                    event.userId,
                    event.groupId,
                    MessageDetail().addText("请输入答案: (${captcha.first})")
                )
                pluginMessageFilter.addInterceptor(object : PluginMessageInterceptor(event.userId) {
                    val answer = captcha.second
                    var retrial = 0

                    override suspend fun handleMessage(msg: Message, bot: QBot) {
                        try {
                            if (msg.message.toString().toInt() == answer) {
                                bot.sendPrivateMsg(
                                    event.userId,
                                    event.groupId,
                                    MessageDetail().addText("你已通过验证")
                                )
                                if (isApproved) {
                                    bot.setGroupBan(event.groupId, event.userId, 0)
                                } else {
                                    bot.setGroupAddRequest(event.flag, event.subType, true)
                                }
                                finished()
                            }
                        } catch (e: Exception) {
                            retrial++
                            if (retrial == 3) {
                                bot.sendPrivateMsg(
                                    event.userId,
                                    event.groupId,
                                    MessageDetail().addText("三次输入错误, 你已被禁止加入")
                                )
                                if (isApproved) {
                                    bot.setGroupKick(event.groupId, event.userId)
                                }
                                finished()
                            }
                            bot.sendPrivateMsg(
                                event.userId,
                                event.groupId,
                                MessageDetail().addText("答案错误, 请重新输入")
                            )
                        }
                        TimeoutJob.addTimeoutJob(scheduler, 2, TimeUnit.MINUTES) {
                            finished()
                        }
                    }
                })
            }

            isApproved = true
        }


    }

    private fun generateCaptcha(): Pair<String, Int> {
        val random = Random()
        var more = random.nextInt(50)
        var less = random.nextInt(50)
        if (less > more) {
            val tmp = less
            less = more
            more = tmp
        }
        return when (random.nextInt(2)) {
            0 -> {
                Pair("$more + $less", more + less)
            }

            1 -> {
                Pair("$more - $less", more - less)
            }

            else -> {
                throw IllegalStateException("Should not happen this state")
            }
        }
    }

    override fun getTypeParameterClass(): KClass<GroupRequest> {
        return GroupRequest::class
    }

}
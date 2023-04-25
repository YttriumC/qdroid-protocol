package cf.vbnm.amoeba.qdroid.bot.job

import cf.vbnm.amoeba.annotation.JobIdentity
import cf.vbnm.amoeba.core.log.Slf4kt
import cf.vbnm.amoeba.qdroid.bot.BotManager
import cf.vbnm.amoeba.qdroid.bot.job.ScheduledJob.Companion.INVOKE_SCHEDULED_GROUP_NAME
import cf.vbnm.amoeba.qdroid.bot.job.ScheduledJob.Companion.INVOKE_SCHEDULED_JOB_NAME
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.quartz.Job
import org.quartz.JobExecutionContext
import org.springframework.stereotype.Component

private val log = Slf4kt.getLogger(ScheduledJob::class.java)

@Component
@JobIdentity(INVOKE_SCHEDULED_JOB_NAME, INVOKE_SCHEDULED_GROUP_NAME)
class ScheduledJob : Job {


    override fun execute(context: JobExecutionContext) {
        context.trigger.jobDataMap?.let {
            val invoke = it["invoke"]
            val botManager = it["botManager"] as BotManager
            log.info("ScheduledJob execute")
            @Suppress("UNCHECKED_CAST")
            invoke as suspend (botManager: BotManager) -> Unit
            @OptIn(DelicateCoroutinesApi::class)
            GlobalScope.launch { invoke(botManager) }
        }
    }

    companion object {
        const val INVOKE_SCHEDULED_JOB_NAME = "scheduledJob"
        const val INVOKE_SCHEDULED_GROUP_NAME = "scheduleGroup"

    }

}
package cf.vbnm.amoeba.qdroid.bot.job

import cf.vbnm.amoeba.annotation.JobIdentity
import cf.vbnm.amoeba.core.log.Slf4kt
import cf.vbnm.amoeba.qdroid.bot.util.toDate
import org.quartz.*
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.util.*
import java.util.concurrent.TimeUnit

const val INVOKE_JOB_GROUP_NAME = "plugin"
const val INVOKE_TIMEOUT_JOB_NAME = "timeoutJob"
private val log = Slf4kt.getLogger(TimeoutJob::class.java)

@Component
@JobIdentity(INVOKE_TIMEOUT_JOB_NAME, INVOKE_JOB_GROUP_NAME)
class TimeoutJob : Job {
    override fun execute(context: JobExecutionContext) {
        context.trigger.jobDataMap?.let {
            val invoke = it["invoke"]
            @Suppress("UNCHECKED_CAST")
            invoke as () -> Unit
            invoke.invoke()
        }
//        context.trigger.jobDataMap?.get("name")?.let {
//            context.scheduler?.unscheduleJob(TriggerKey.triggerKey(it.toString(), INVOKE_JOB_GROUP_NAME))
//        }

    }

    companion object {
        fun addTimeoutJob(
            scheduler: Scheduler,
            duration: Long,
            timeUnit: TimeUnit,
            invoke: () -> Unit
        ): Pair<String, String> {
            val name = UUID.randomUUID().toString()
            scheduler.scheduleJob(
                TriggerBuilder.newTrigger().run {
                    startAt(LocalDateTime.now().plusSeconds(timeUnit.toSeconds(duration)).toDate())
                    forJob(INVOKE_TIMEOUT_JOB_NAME, INVOKE_JOB_GROUP_NAME)
                    usingJobData(JobDataMap(mutableMapOf(Pair("invoke", invoke), Pair("name", name))))
                    withIdentity(name, INVOKE_JOB_GROUP_NAME)
                    build()
                }
            )
            log.info("add timeout trigger")
            return Pair(name, INVOKE_JOB_GROUP_NAME)
        }

        fun cancelTimeoutJob(scheduler: Scheduler, identity: Pair<String, String>) {
            log.info("cancel timeout trigger")
            scheduler.unscheduleJob(TriggerKey.triggerKey(identity.first, identity.second))
        }
    }
}



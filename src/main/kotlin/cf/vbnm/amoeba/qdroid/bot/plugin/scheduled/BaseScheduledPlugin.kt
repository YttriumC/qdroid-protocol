package cf.vbnm.amoeba.qdroid.bot.plugin.scheduled

import cf.vbnm.amoeba.core.CoreProperty
import cf.vbnm.amoeba.core.log.Slf4kt
import cf.vbnm.amoeba.qdroid.bot.BotManager
import cf.vbnm.amoeba.qdroid.bot.QBot
import cf.vbnm.amoeba.qdroid.bot.job.ScheduledJob
import cf.vbnm.amoeba.qdroid.bot.util.toDate
import kotlinx.coroutines.runBlocking
import org.quartz.*
import org.springframework.beans.factory.annotation.Autowired
import java.time.LocalDateTime
import java.util.*
import java.util.concurrent.TimeUnit

private val log = Slf4kt.getLogger(BaseScheduledPlugin::class.java)

abstract class BaseScheduledPlugin(
    protected val coreProperty: CoreProperty
) {
    private lateinit var _scheduler: Scheduler
    private lateinit var botManager: BotManager
    protected val scheduler: Scheduler
        get() = _scheduler

    @Autowired
    fun setScheduler(scheduler: Scheduler) {
        this._scheduler = scheduler
    }

    @Autowired
    fun setBotManager(botManager: BotManager) {
        this.botManager = botManager
    }

    abstract suspend fun apply(bot: QBot)

    open fun initTrigger() {}

    fun addCronTrigger(cron: String): Pair<String, String> {
        val name = UUID.randomUUID().toString()
        val invoke = suspend() {
            it.forEach { _, bot ->
                runBlocking { apply(bot) }
            }
        } /*fun(botManager: BotManager) {

        }*/
        scheduler.scheduleJob(TriggerBuilder.newTrigger().run {
            withSchedule(CronScheduleBuilder.cronSchedule(cron.trim()))
            usingJobData(JobDataMap(mutableMapOf(Pair("invoke", invoke), Pair("botManager", botManager))))
            startNow()
            forJob(ScheduledJob.INVOKE_SCHEDULED_JOB_NAME, ScheduledJob.INVOKE_SCHEDULED_GROUP_NAME)
            withIdentity(name, ScheduledJob.INVOKE_SCHEDULED_GROUP_NAME)
            build()
        })
        return Pair(name, ScheduledJob.INVOKE_SCHEDULED_GROUP_NAME).also { log.info("added a cron trigger:, {}", it) }
    }

    fun addCronTriggerWithTask(cron: String, invoke: suspend (botManager: BotManager) -> Unit): Pair<String, String> {
        val name = UUID.randomUUID().toString()
        scheduler.scheduleJob(TriggerBuilder.newTrigger().run {
            withSchedule(CronScheduleBuilder.cronSchedule(cron.trim())); startNow()
            usingJobData(JobDataMap(mutableMapOf(Pair("invoke", invoke), Pair("botManager", botManager))))
            forJob(ScheduledJob.INVOKE_SCHEDULED_JOB_NAME, ScheduledJob.INVOKE_SCHEDULED_GROUP_NAME)
            withIdentity(name, ScheduledJob.INVOKE_SCHEDULED_GROUP_NAME); build()
        })
        return Pair(name, ScheduledJob.INVOKE_SCHEDULED_GROUP_NAME).also { log.info("added a cron trigger:, {}", it) }
    }

    /*
        fun addFixDelayTrigger(
            delay: Long,
            timeUnit: TimeUnit,
            initialDelay: Long,
            invoke: (botManager: BotManager) -> Unit
        ): Pair<String, String> {
            val name = UUID.randomUUID().toString()
            scheduler.scheduleJob(TriggerBuilder.newTrigger().run {
                startAt(LocalDateTime.now().plusSeconds(timeUnit.toSeconds(initialDelay)).toDate())
                usingJobData(JobDataMap(mutableMapOf(Pair("invoke", invoke), Pair("botManager", botManager))))
                forJob(ScheduledJob.INVOKE_SCHEDULED_JOB_NAME, ScheduledJob.INVOKE_SCHEDULED_GROUP_NAME)
                withIdentity(name, ScheduledJob.INVOKE_SCHEDULED_GROUP_NAME)
                build()
            })
            return Pair(name, ScheduledJob.INVOKE_SCHEDULED_GROUP_NAME)
        }*/

    fun addFixRateTriggerWithTask(
        delay: Long,
        timeUnit: TimeUnit,
        initialDelay: Long,
        invoke: suspend (botManager: BotManager) -> Unit
    ): Pair<String, String> {
        val name = UUID.randomUUID().toString()
        scheduler.scheduleJob(TriggerBuilder.newTrigger().run {
            withSchedule(
                SimpleScheduleBuilder.simpleSchedule().withIntervalInMilliseconds(timeUnit.toMillis(delay))
                    .repeatForever().withMisfireHandlingInstructionIgnoreMisfires()
            )
            startAt(LocalDateTime.now().plusSeconds(timeUnit.toSeconds(initialDelay)).toDate())
            usingJobData(JobDataMap(mutableMapOf(Pair("invoke", invoke), Pair("botManager", botManager))))
            forJob(ScheduledJob.INVOKE_SCHEDULED_JOB_NAME, ScheduledJob.INVOKE_SCHEDULED_GROUP_NAME)
            withIdentity(name, ScheduledJob.INVOKE_SCHEDULED_GROUP_NAME)
            build()
        })
        return Pair(name, ScheduledJob.INVOKE_SCHEDULED_GROUP_NAME).also {
            log.info("added a fix rate trigger: {}", it)
        }
    }
}

@kotlin.internal.InlineOnly
@Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")
inline fun <R> suspend(noinline block: suspend (BotManager) -> R): suspend (BotManager) -> R = block
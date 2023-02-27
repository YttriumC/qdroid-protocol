package cf.vbnm.amoeba.qdroid.bot.plugin

@Target(AnnotationTarget.TYPE)
@Retention(AnnotationRetention.RUNTIME)
annotation class PluginTarget(
    val value: String, val order: Int = 100
)

package cf.vbnm.amoeba.qdroid.bot.plugin

/**
 * 越小越靠前
 * */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
annotation class PluginOrder(val order: Int = 500)

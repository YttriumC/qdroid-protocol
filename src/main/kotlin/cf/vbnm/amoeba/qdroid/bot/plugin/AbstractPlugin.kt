package cf.vbnm.amoeba.qdroid.bot.plugin

import cf.vbnm.amoeba.web.service.PropertyService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
abstract class AbstractPlugin {
    private lateinit var propertyService: PropertyService

    @Autowired
    fun setPropertyService(propertyService: PropertyService) {
        this.propertyService = propertyService
    }

    open fun getPluginName(): String {
        return this::class.simpleName ?: "unmapped"
    }

    operator fun set(name: String, value: String) {
        propertyService["plugin.${getPluginName()}.${name.trim().removePrefix(".")}"] = value
    }

    operator fun get(name: String): String? {
        return propertyService["plugin.${getPluginName()}.${name.trim().removePrefix(".")}"]
    }

    open fun getPropKey(name: String): String {
        return "plugin.${getPluginName()}.${name.trim().removePrefix(".")}"
    }

    fun deleteProperty(name: String) {
        propertyService.delete("plugin.${getPluginName()}.${name.trim().removePrefix(".")}")
    }

    fun isAdmin(id: Long): Boolean {
        return id.toString() == propertyService[ADMIN_ACCOUNT_KEY]
    }

    fun <R> doIfAdmin(id: Long, invoke: () -> R): R? {
        if (isAdmin(id)) {
            return invoke()
        }
        return null
    }

    companion object {
        const val ADMIN_ACCOUNT_KEY = "plugin.auth.admin.id"
    }
}
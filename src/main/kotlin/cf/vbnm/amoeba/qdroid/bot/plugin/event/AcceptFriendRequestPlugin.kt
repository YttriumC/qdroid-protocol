package cf.vbnm.amoeba.qdroid.bot.plugin.event

import cf.vbnm.amoeba.core.CoreProperty
import cf.vbnm.amoeba.core.log.Slf4kt
import cf.vbnm.amoeba.qdroid.bot.QBot
import cf.vbnm.amoeba.qdroid.cq.events.request.FriendRequest
import org.springframework.stereotype.Component
import kotlin.reflect.KClass

private val log = Slf4kt.getLogger(AcceptFriendRequestPlugin::class.java)

@Component
class AcceptFriendRequestPlugin(
    coreProperty: CoreProperty,
    pluginMessageFilter: PluginMessageFilter
) : BaseEventPlugin<FriendRequest>(coreProperty, pluginMessageFilter) {
    override fun getTypeParameterClass(): KClass<FriendRequest> {
        return FriendRequest::class
    }

    override suspend fun apply(bot: QBot, event: FriendRequest) {
        log.info("Add friend: {}", event.userId)
        bot.setFriendAddRequest(event.flag, true, "")
    }
}
package cf.vbnm.amoeba.qdroid.cq.events

import cf.vbnm.amoeba.qdroid.cq.events.notice.*
import cf.vbnm.amoeba.qdroid.cq.events.enums.PostNoticeType
import cf.vbnm.amoeba.qdroid.cq.events.enums.PostType
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.ObjectMapper

abstract class Notice(
    @JsonProperty("self_id")
    selfId: Long,
    @JsonProperty("time")
    time: Int,
    @JsonProperty("notice_type")
    val noticeType: PostNoticeType
) : BasePostEvent(selfId, time, PostType.NOTICE) {

    override fun toNotice(): Notice {
        return this
    }

    open fun toClientStatus(): ClientStatus {
        throw TypeCastException("Cannot cast notice $noticeType to ClientStatus")
    }

    fun isClientStatus(): Boolean {
        return noticeType == PostNoticeType.CLIENT_STATUS
    }

    open fun toEssence(): Essence {
        throw TypeCastException("Cannot cast notice $noticeType to Essence")
    }

    fun isEssence(): Boolean {
        return noticeType == PostNoticeType.ESSENCE
    }

    open fun toFriendAdd(): FriendAdd {
        throw TypeCastException("Cannot cast notice $noticeType to FriendAdd")
    }

    fun isFriendAdd(): Boolean {
        return noticeType == PostNoticeType.FRIEND_ADD
    }

    open fun toFriendRecall(): FriendRecall {
        throw TypeCastException("Cannot cast notice $noticeType to FriendRecall")
    }

    fun isFriendRecall(): Boolean {
        return noticeType == PostNoticeType.FRIEND_RECALL
    }

    open fun toGroupAdmin(): GroupAdmin {
        throw TypeCastException("Cannot cast notice $noticeType to GroupAdmin")
    }

    fun isGroupAdmin(): Boolean {
        return noticeType == PostNoticeType.GROUP_ADMIN
    }

    open fun toGroupBan(): GroupBan {
        throw TypeCastException("Cannot cast notice $noticeType to GroupBan")
    }

    fun isGroupBan(): Boolean {
        return noticeType == PostNoticeType.GROUP_BAN
    }

    open fun toGroupCard(): GroupCard {
        throw TypeCastException("Cannot cast notice $noticeType to GroupCard")
    }

    fun isGroupCard(): Boolean {
        return noticeType == PostNoticeType.GROUP_CARD
    }

    open fun toGroupDecrease(): GroupDecrease {
        throw TypeCastException("Cannot cast notice $noticeType to GroupDecrease")
    }

    fun isGroupDecrease(): Boolean {
        return noticeType == PostNoticeType.GROUP_DECREASE
    }

    open fun toGroupIncrease(): GroupIncrease {
        throw TypeCastException("Cannot cast notice $noticeType to GroupIncrease")
    }

    fun isGroupIncrease(): Boolean {
        return noticeType == PostNoticeType.GROUP_INCREASE
    }

    open fun toGroupRecall(): GroupRecall {
        throw TypeCastException("Cannot cast notice $noticeType to FriendRequest")
    }

    fun isGroupRecall(): Boolean {
        return noticeType == PostNoticeType.GROUP_RECALL
    }

    open fun toGroupUpload(): GroupUpload {
        throw TypeCastException("Cannot cast notice $noticeType to GroupUpload")
    }

    fun isGroupUpload(): Boolean {
        return noticeType == PostNoticeType.GROUP_UPLOAD
    }

    open fun toNotify(): Notify {
        throw TypeCastException("Cannot cast notice $noticeType to Notify")
    }

    fun isNotify(): Boolean {
        return noticeType == PostNoticeType.NOTIFY
    }

    open fun toOfflineFile(): OfflineFile {
        throw TypeCastException("Cannot cast notice $noticeType to OfflineFile")
    }

    fun isOfflineFile(): Boolean {
        return noticeType == PostNoticeType.OFFLINE_FILE
    }

    override fun toString(): String {
        return "noticeType=$noticeType, ${super.toString()}"
    }

    companion object {
        fun parseEvent(map: Map<String, Any?>, objectMapper: ObjectMapper): BasePostEvent {
            return when (PostNoticeType.parseType(map["notice_type"].toString())) {
                PostNoticeType.GROUP_UPLOAD -> GroupUpload.parseEvent(map, objectMapper)
                PostNoticeType.GROUP_ADMIN -> GroupAdmin.parseEvent(map, objectMapper)
                PostNoticeType.GROUP_DECREASE -> GroupDecrease.parseEvent(map, objectMapper)
                PostNoticeType.GROUP_INCREASE -> GroupIncrease.parseEvent(map, objectMapper)
                PostNoticeType.GROUP_BAN -> GroupBan.parseEvent(map, objectMapper)
                PostNoticeType.FRIEND_ADD -> FriendAdd.parseEvent(map, objectMapper)
                PostNoticeType.GROUP_RECALL -> GroupRecall.parseEvent(map, objectMapper)
                PostNoticeType.FRIEND_RECALL -> FriendRecall.parseEvent(map, objectMapper)
                PostNoticeType.GROUP_CARD -> GroupCard.parseEvent(map, objectMapper)
                PostNoticeType.OFFLINE_FILE -> OfflineFile.parseEvent(map, objectMapper)
                PostNoticeType.CLIENT_STATUS -> ClientStatus.parseEvent(map, objectMapper)
                PostNoticeType.ESSENCE -> Essence.parseEvent(map, objectMapper)
                PostNoticeType.NOTIFY -> Notify.parseEvent(map, objectMapper)
            }
        }
    }

}
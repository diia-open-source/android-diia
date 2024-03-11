package ua.gov.diia.notifications.models.notification.pull

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PullNotification(
    @Json(name = "notificationId")
    val notificationId: String?,
    @Json(name = "creationDate")
    val creationDate: String?,
    @Json(name = "isRead")
    var isRead: Boolean?,
    @Json(name = "message")
    val pullNotificationMessage: PullNotificationMessage?,
    @Json(name = "local_sync_action")
    var syncAction: PullNotificationSyncAction? = PullNotificationSyncAction.NONE
) {
    companion object {
        const val TYPE_GREETING = "greeting"
        const val TYPE_URGENT = "urgent"
        const val TYPE_REMINDER = "reminder"
        const val TYPE_ATTENTION = "attention"
        const val TYPE_CONFIRMATION = "confirmation"
    }
}
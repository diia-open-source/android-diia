package ua.gov.diia.notifications.models.notification.pull

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PullNotificationsResponse(
    @Json(name = "notifications")
    val notifications: List<PullNotification>,
    @Json(name = "total")
    val total: Int,
    @Json(name = "unread")
    val unread: Int
)
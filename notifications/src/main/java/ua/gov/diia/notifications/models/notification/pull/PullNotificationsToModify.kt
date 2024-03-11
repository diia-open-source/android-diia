package ua.gov.diia.notifications.models.notification.pull

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PullNotificationsToModify(
    @Json(name = "notificationIds")
    val messageIds: List<String>
)
package ua.gov.diia.core.models.notification.pull.message

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Notification(
    @Json(name = "title")
    val title: String,
    @Json(name = "body")
    val body: List<NotificationMessagesBody>,
)
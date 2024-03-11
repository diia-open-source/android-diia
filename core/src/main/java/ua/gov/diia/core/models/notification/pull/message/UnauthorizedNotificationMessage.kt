package ua.gov.diia.core.models.notification.pull.message

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UnauthorizedNotificationMessage(
    @Json(name = "message")
    val message: Notification
)
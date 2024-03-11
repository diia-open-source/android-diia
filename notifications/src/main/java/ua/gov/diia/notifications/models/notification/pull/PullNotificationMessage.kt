package ua.gov.diia.notifications.models.notification.pull

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import ua.gov.diia.core.models.notification.push.PushAction

@JsonClass(generateAdapter = true)
data class PullNotificationMessage(
    @Json(name = "icon")
    val icon: String,
    @Json(name = "title")
    val title: String,
    @Json(name = "shortText")
    val shortText: String,
    @Json(name = "action")
    val action: PushAction?
)
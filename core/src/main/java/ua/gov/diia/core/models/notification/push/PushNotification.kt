package ua.gov.diia.core.models.notification.push

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PushNotification(
    @Json(name = "action")
    val action: PushAction,
    @Json(name = "needAuth")
    val needAuth: Boolean?,
    @Json(name = "notificationId")
    var notificationId: String?,
    @Json(name = "shortText")
    val shortText: String?,
    @Json(name = "title")
    val title: String?,
    @Json(name = "unread")
    val unread: Int?
)
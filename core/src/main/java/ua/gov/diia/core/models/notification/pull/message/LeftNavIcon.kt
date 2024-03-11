package ua.gov.diia.core.models.notification.pull.message


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LeftNavIcon(
    @Json(name = "action")
    val action: Action?,
    @Json(name = "code")
    val code: String?,
    @Json(name = "accessibilityDescription")
    val accessibilityDescription: String?
)
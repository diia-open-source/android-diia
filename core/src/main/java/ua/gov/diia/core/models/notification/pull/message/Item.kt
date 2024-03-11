package ua.gov.diia.core.models.notification.pull.message


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Item(
    @Json(name = "action")
    val action: Action?,
    @Json(name = "description")
    val description: String?,
    @Json(name = "iconLeft")
    val iconLeft: String?,
    @Json(name = "iconRight")
    val iconRight: String?,
    @Json(name = "label")
    val label: String?,
    @Json(name = "logoLeft")
    val logoLeft: String?,
    @Json(name = "state")
    val state: String?
)
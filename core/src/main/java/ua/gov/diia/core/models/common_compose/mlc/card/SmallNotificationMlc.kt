package ua.gov.diia.core.models.common_compose.mlc.card


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import ua.gov.diia.core.models.common_compose.general.Action

@JsonClass(generateAdapter = true)
data class SmallNotificationMlc(
    @Json(name = "id")
    val id: String,
    @Json(name = "label")
    val label: String,
    @Json(name = "text")
    val text: String,
    @Json(name = "action")
    val action: Action?,
)
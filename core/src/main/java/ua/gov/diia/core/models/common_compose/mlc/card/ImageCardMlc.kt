package ua.gov.diia.core.models.common_compose.mlc.card


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import ua.gov.diia.core.models.common_compose.general.Action

@JsonClass(generateAdapter = true)
data class ImageCardMlc(
    @Json(name = "action")
    val action: Action?,
    @Json(name = "iconRight")
    val iconRight: String,
    @Json(name = "image")
    val image: String,
    @Json(name = "label")
    val label: String,
    @Json(name = "componentId")
    val componentId: String? = null
)
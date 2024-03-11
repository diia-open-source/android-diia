package ua.gov.diia.core.models.common_compose.mlc.card


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import ua.gov.diia.core.models.common_compose.general.Action

@JsonClass(generateAdapter = true)
data class IconCardMlc(
    @Json(name = "action")
    val action: Action?,
    @Json(name = "iconLeft")
    val iconLeft: String,
    @Json(name = "label")
    val label: String
)
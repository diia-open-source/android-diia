package ua.gov.diia.core.models.common_compose.atm.icon


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import ua.gov.diia.core.models.common_compose.general.Action

@JsonClass(generateAdapter = true)
data class IconUrlAtm(
    @Json(name = "accessibilityDescription")
    val accessibilityDescription: String?,
    @Json(name = "action")
    val action: Action?,
    @Json(name = "componentId")
    val componentId: String?,
    @Json(name = "url")
    val url: String?
)
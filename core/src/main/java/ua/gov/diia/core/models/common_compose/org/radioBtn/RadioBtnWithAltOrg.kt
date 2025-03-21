package ua.gov.diia.core.models.common_compose.org.radioBtn


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RadioBtnWithAltOrg(
    @Json(name = "componentId")
    val componentId: String?,
    @Json(name = "items")
    val items: List<Item>?
)
@JsonClass(generateAdapter = true)
data class Item(
    @Json(name = "radioBtnGroupOrg")
    val radioBtnGroupOrg: RadioBtnGroupOrg?
)
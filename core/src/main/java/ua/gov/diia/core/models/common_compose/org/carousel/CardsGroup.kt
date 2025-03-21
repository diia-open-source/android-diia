package ua.gov.diia.core.models.common_compose.org.carousel


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CardsGroup(
    @Json(name = "items")
    val items: List<Item>?
)
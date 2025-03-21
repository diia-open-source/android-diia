package ua.gov.diia.core.models.common_compose.org.carousel


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CardHorizontalScrollOrg(
    @Json(name = "cardsGroup")
    val cardsGroup: List<CardsGroup>?,
    @Json(name = "componentId")
    val componentId: String?
)
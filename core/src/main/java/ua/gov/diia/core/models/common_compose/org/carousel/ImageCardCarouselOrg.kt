package ua.gov.diia.core.models.common_compose.org.carousel


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import ua.gov.diia.core.models.common_compose.atm.indicators.DotNavigationAtm
import ua.gov.diia.core.models.common_compose.mlc.card.ImageCardMlc

@JsonClass(generateAdapter = true)
data class ImageCardCarouselOrg(
    @Json(name = "componentId")
    val componentId: String?,
    @Json(name = "dotNavigationAtm")
    val dotNavigationAtm: DotNavigationAtm?,
    @Json(name = "items")
    val items: List<Item>
) {
    @JsonClass(generateAdapter = true)
    data class Item(
        @Json(name = "imageCardMlc")
        val imageCardMlc: ImageCardMlc?
    )
}
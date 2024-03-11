package ua.gov.diia.core.models.common_compose.org.carousel


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import ua.gov.diia.core.models.common_compose.atm.indicators.DotNavigationAtm
import ua.gov.diia.core.models.common_compose.mlc.card.IconCardMlc
import ua.gov.diia.core.models.common_compose.mlc.card.SmallNotificationMlc

@JsonClass(generateAdapter = true)
data class SmallNotificationCarouselOrg(
    @Json(name = "dotNavigationAtm")
    val dotNavigationAtm: DotNavigationAtm,
    @Json(name = "items")
    val items: List<Item>
) {
    @JsonClass(generateAdapter = true)
    data class Item(
        @Json(name = "smallNotificationMlc")
        val smallNotificationMlc: SmallNotificationMlc?,
        @Json(name = "iconCardMlc")
        val iconCardMlc: IconCardMlc?
    )
}
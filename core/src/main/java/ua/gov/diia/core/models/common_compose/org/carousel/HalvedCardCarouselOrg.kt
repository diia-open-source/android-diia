package ua.gov.diia.core.models.common_compose.org.carousel


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import ua.gov.diia.core.models.common_compose.atm.indicators.DotNavigationAtm
import ua.gov.diia.core.models.common_compose.mlc.card.HalvedCardMlc
import ua.gov.diia.core.models.common_compose.mlc.card.IconCardMlc

@JsonClass(generateAdapter = true)
data class HalvedCardCarouselOrg(
    @Json(name = "dotNavigationAtm")
    val dotNavigationAtm: DotNavigationAtm,
    @Json(name = "items")
    val items: List<Item>
) {
    @JsonClass(generateAdapter = true)
    data class Item(
        @Json(name = "halvedCardMlc")
        val halvedCardMlc: HalvedCardMlc?,
        @Json(name = "iconCardMlc")
        val iconCardMlc: IconCardMlc?
    )
}


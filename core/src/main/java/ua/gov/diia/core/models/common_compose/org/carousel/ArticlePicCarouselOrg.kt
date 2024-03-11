package ua.gov.diia.core.models.common_compose.org.carousel

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import ua.gov.diia.core.models.common_compose.atm.indicators.DotNavigationAtm
import ua.gov.diia.core.models.common_compose.atm.media.ArticlePicAtm
import ua.gov.diia.core.models.common_compose.mlc.media.ArticleVideoMlc

@JsonClass(generateAdapter = true)
data class ArticlePicCarouselOrg(
    @Json(name = "dotNavigationAtm")
    val dotNavigationAtm: DotNavigationAtm,
    @Json(name = "items")
    val items: List<Item>
) {
    @JsonClass(generateAdapter = true)
    data class Item(
        @Json(name = "articlePicAtm")
        val articlePicAtm: ArticlePicAtm?,
        @Json(name = "articleVideoMlc")
        val articleVideoMlc: ArticleVideoMlc?
    )
}
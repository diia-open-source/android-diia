package ua.gov.diia.core.models.common_compose.atm.media

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ArticlePicAtm(
    @Json(name = "image")
    val image: String
)

@JsonClass(generateAdapter = true)
data class ArticlePicAtmItem(
    @Json(name = "articlePicAtm")
    val articlePicAtm: ArticlePicAtm
)

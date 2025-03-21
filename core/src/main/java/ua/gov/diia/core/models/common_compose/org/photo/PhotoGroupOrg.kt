package ua.gov.diia.core.models.common_compose.org.photo

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import ua.gov.diia.core.models.common_compose.atm.media.ArticlePicAtmItem

@JsonClass(generateAdapter = true)
data class PhotoGroupOrg(
    @Json(name = "componentId")
    val componentId: String?,
    @Json(name = "pictures")
    val pictures: List<ArticlePicAtmItem>
)

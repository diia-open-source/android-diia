package ua.gov.diia.core.models.notification.pull.message


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ArticleVideoMlc(
    @Json(name = "source")
    val source: String?,
    @Json(name = "playerBtnAtm")
    val playerBtnAtm: PlayerBtnAtm?
)
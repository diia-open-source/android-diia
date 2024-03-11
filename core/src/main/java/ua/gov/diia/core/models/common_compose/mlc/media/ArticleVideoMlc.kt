package ua.gov.diia.core.models.common_compose.mlc.media


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import ua.gov.diia.core.models.common_compose.atm.button.PlayerBtnAtm

@JsonClass(generateAdapter = true)
data class ArticleVideoMlc(
    @Json(name = "playerBtnAtm")
    val playerBtnAtm: PlayerBtnAtm?,
    @Json(name = "source")
    val source: String
)
package ua.gov.diia.core.models.common_compose.org.media

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import ua.gov.diia.core.models.common_compose.atm.button.BtnPlainIconAtm

@JsonClass(generateAdapter = true)
data class SingleMediaUploadGroupOrg(
    @Json(name = "componentId")
    val componentId: String?,
    @Json(name = "title")
    val title: String?,
    @Json(name = "description")
    val description: String?,
    @Json(name = "aspectRatio")
    val aspectRatio: String?,
    @Json(name = "btnPlainIconAtm")
    val btnPlainIconAtm: BtnPlainIconAtm
)

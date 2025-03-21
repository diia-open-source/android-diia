package ua.gov.diia.core.models.common_compose.org.media

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import ua.gov.diia.core.models.common_compose.atm.button.BtnPlainIconAtm

@JsonClass(generateAdapter = true)
data class MediaUploadGroupOrg(
    @Json(name = "componentId")
    val componentId: String?,
    @Json(name = "title")
    val title: String?,
    @Json(name = "description")
    val description: String?,
    @Json(name = "maxCount")
    val maxCount: Int?,
    @Json(name = "btnPlainIconAtm")
    val btnPlainIconAtm: BtnPlainIconAtm
)
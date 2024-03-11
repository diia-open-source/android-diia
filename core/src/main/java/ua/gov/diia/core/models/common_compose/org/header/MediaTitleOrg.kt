package ua.gov.diia.core.models.common_compose.org.header


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import ua.gov.diia.core.models.common_compose.atm.button.BtnPlainIconAtm

@JsonClass(generateAdapter = true)
data class MediaTitleOrg(
    @Json(name = "btnPlainIconAtm")
    val btnPlainIconAtm: BtnPlainIconAtm,
    @Json(name = "secondaryLabel")
    val secondaryLabel: String,
    @Json(name = "title")
    val title: String
)
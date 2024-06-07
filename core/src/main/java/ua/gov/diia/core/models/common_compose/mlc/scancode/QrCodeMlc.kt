package ua.gov.diia.core.models.common_compose.mlc.scancode


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class QrCodeMlc(
    @Json(name = "qrLink")
    val qrLink: String,
    @Json(name = "componentId")
    val componentId: String?
)
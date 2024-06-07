package ua.gov.diia.core.models.common_compose.mlc.divider


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class DividerLineMlc(
    @Json(name = "componentId")
    val componentId: String? = null,
    @Json(name = "type")
    val type: String?
)
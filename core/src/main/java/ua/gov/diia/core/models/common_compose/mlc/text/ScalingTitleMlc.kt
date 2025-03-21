package ua.gov.diia.core.models.common_compose.mlc.text

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ScalingTitleMlc(
    @Json(name = "componentId")
    val componentId: String?,
    @Json(name = "label")
    val label: String
)
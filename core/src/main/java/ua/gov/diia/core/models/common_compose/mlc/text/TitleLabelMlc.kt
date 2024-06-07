package ua.gov.diia.core.models.common_compose.mlc.text

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TitleLabelMlc(
    @Json(name = "label")
    val label: String,
    @Json(name = "componentId")
    val componentId: String?,
)
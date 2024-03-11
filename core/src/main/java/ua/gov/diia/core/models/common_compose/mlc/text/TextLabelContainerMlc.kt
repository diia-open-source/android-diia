package ua.gov.diia.core.models.common_compose.mlc.text

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
typealias TextParameterApi = ua.gov.diia.core.models.common.message.TextParameter

@JsonClass(generateAdapter = true)
data class TextLabelContainerMlc(
    @Json(name = "label")
    val label: String?,
    @Json(name = "text")
    val text: String?,
    @Json(name = "parameters")
    val parameters: List<TextParameterApi>?
)
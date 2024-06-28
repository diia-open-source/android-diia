
package ua.gov.diia.core.models.common_compose.mlc.text

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import ua.gov.diia.core.models.common.message.TextParameter

@JsonClass(generateAdapter = true)
data class TextLabelMlc(
    @Json(name = "componentId")
    val componentId: String? = null,
    @Json(name = "label")
    val label: String?,
    @Json(name = "text")
    val text: String,
    @Json(name = "parameters")
    val parameters: List<TextParameter>?
)
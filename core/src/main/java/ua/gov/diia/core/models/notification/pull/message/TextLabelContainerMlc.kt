package ua.gov.diia.core.models.notification.pull.message


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import ua.gov.diia.core.models.common.message.TextParameter

@JsonClass(generateAdapter = true)
data class TextLabelContainerMlc(
    @Json(name = "parameters")
    val parameters: List<TextParameter?>,
    @Json(name = "text")
    val text: String?,
    @Json(name = "label")
    val label: String?
)
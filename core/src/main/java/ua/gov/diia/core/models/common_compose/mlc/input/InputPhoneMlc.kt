package ua.gov.diia.core.models.common_compose.mlc.input


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class InputPhoneMlc(
    @Json(name = "componentId")
    val componentId: String?,
    @Json(name = "inputCode")
    val inputCode: String?,
    @Json(name = "label")
    val label: String?,
    @Json(name = "placeholder")
    val placeholder: String?,
    @Json(name = "hint")
    val hint: String?,
    @Json(name = "mask")
    val mask: String?,
    @Json(name = "value")
    val value: String?,
    @Json(name = "mandatory")
    val mandatory: Boolean?,
    @Json(name = "validation")
    val validation: List<Validation>?
)
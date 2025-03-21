package ua.gov.diia.core.models.common_compose.mlc.input


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class InputNumberMlc(
    @Json(name = "componentId")
    val componentId: String?,
    @Json(name = "errorMessage")
    val errorMessage: String?,
    @Json(name = "hint")
    val hint: String?,
    @Json(name = "inputCode")
    val inputCode: String?,
    @Json(name = "label")
    val label: String,
    @Json(name = "mandatory")
    val mandatory: Boolean?,
    @Json(name = "maxValue")
    val maxValue: Long?,
    @Json(name = "minValue")
    val minValue: Long?,
    @Json(name = "placeholder")
    val placeholder: String?,
    @Json(name = "value")
    val value: Long?
)
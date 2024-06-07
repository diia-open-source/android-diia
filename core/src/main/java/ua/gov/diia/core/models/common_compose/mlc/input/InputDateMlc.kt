package ua.gov.diia.core.models.common_compose.mlc.input

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class InputDateMlc(
    @Json(name = "componentId")
    val componentId: String? = null,
    @Json(name = "id")
    val id: String?,
    @Json(name = "blocker")
    val blocker: Boolean?,
    @Json(name = "label")
    val label: String,
    @Json(name = "value")
    val value: String?,
    @Json(name = "hint")
    val hint: String?,
    @Json(name = "validation")
    val validation: List<ValidationDateItem>?
)

@JsonClass(generateAdapter = true)
data class ValidationDateItem(
    @Json(name = "regexp")
    val regexp: String,
    @Json(name = "flags")
    val flags: List<String>,
    @Json(name = "errorMessage")
    val errorMessage: String
)
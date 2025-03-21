package ua.gov.diia.core.models.common_compose.org.input


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import ua.gov.diia.core.models.common_compose.mlc.input.InputPhoneMlc
import ua.gov.diia.core.models.common_compose.mlc.input.Validation

@JsonClass(generateAdapter = true)
data class InputPhoneCodeOrg(
    @Json(name = "componentId")
    val componentId: String?,
    @Json(name = "label")
    val label: String,
    @Json(name = "hint")
    val hint: String?,
    @Json(name = "mandatory")
    val mandatory: Boolean?,
    @Json(name = "inputCode")
    val inputCode: String,
    @Json(name = "inputPhoneMlc")
    val inputPhoneMlc: InputPhoneMlc,
    @Json(name = "codeValueId")
    val codeValueId: String?,
    @Json(name = "codeValueIsEditable")
    val codeValueIsEditable: Boolean?,
    @Json(name = "codes")
    val codes: List<Code>?
) {
    @JsonClass(generateAdapter = true)
    data class Code(
        @Json(name = "id")
        val id: String,
        @Json(name = "maskCode")
        val maskCode: String?,
        @Json(name = "placeholder")
        val placeholder: String?,
        @Json(name = "label")
        val label: String,
        @Json(name = "description")
        val description: String,
        @Json(name = "value")
        val value: String,
        @Json(name = "icon")
        val icon: String,
        @Json(name = "validation")
        val validation: List<Validation>?
    )
}
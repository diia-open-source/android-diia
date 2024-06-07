package ua.gov.diia.core.models.common_compose.org.input

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import ua.gov.diia.core.models.common_compose.mlc.input.InputTextMlc

@JsonClass(generateAdapter = true)
data class EditAutomaticallyDeterminedValueOrg(
    @Json(name = "componentId")
    val componentId: String?,
    @Json(name = "title")
    val title: String?,
    @Json(name = "label")
    val label: String?,
    @Json(name = "value")
    val value: String?,
    @Json(name = "inputTextMlc")
    val inputTextMlc: InputTextMlc?
)
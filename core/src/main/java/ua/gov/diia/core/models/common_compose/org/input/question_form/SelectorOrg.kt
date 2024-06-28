package ua.gov.diia.core.models.common_compose.org.input.question_form

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SelectorOrg(
    @Json(name = "componentId")
    val componentId: String? = null,
    @Json(name = "id")
    val id: String?,
    @Json(name = "blocker")
    val blocker: Boolean?,
    @Json(name = "label")
    val label: String,
    @Json(name = "placeholder")
    val placeholder: String,
    @Json(name = "hint")
    val hint: String?,
    @Json(name = "value")
    val value: String?,
    @Json(name = "selectorListWidgetOrg")
    val selectorListWidgetOrg: SelectorListWidgetOrg?,
)
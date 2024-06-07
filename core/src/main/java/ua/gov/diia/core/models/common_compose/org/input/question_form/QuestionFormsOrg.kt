package ua.gov.diia.core.models.common_compose.org.input.question_form

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class QuestionFormsOrg(
    @Json(name = "componentId")
    val componentId: String? = null,
    @Json(name = "id")
    val id: String,
    @Json(name = "title")
    val title: String?,
    @Json(name = "condition")
    val condition: String?,
    @Json(name = "items")
    val items: List<QuestionFormsOrgItem>
)
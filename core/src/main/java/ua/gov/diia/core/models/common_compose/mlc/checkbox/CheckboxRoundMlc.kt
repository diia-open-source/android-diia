package ua.gov.diia.core.models.common_compose.mlc.checkbox

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CheckboxRoundMlc(
    @Json(name = "id")
    val id: String?,
    @Json(name = "componentId")
    val componentId: String?,
    @Json(name = "label")
    val label: String,
    @Json(name = "description")
    val description: String?,
    @Json(name = "status")
    val status: String?,
    @Json(name = "state")
    val state: String?
)
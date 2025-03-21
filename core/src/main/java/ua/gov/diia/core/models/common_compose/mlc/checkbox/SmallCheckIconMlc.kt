package ua.gov.diia.core.models.common_compose.mlc.checkbox

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SmallCheckIconMlc(
    @Json(name = "componentId")
    val componentId: String?,
    @Json(name = "code")
    val code: String?,
    @Json(name = "icon")
    val icon: String,
    @Json(name = "label")
    val label: String,
    @Json(name = "state")
    val state: String?
)
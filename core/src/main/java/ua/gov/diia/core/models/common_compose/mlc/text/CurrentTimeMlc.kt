package ua.gov.diia.core.models.common_compose.mlc.text

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import ua.gov.diia.core.models.common_compose.general.Action

@JsonClass(generateAdapter = true)
data class CurrentTimeMlc(
    @Json(name = "label")
    val label: String? = null,
    @Json(name = "action")
    val action: Action? = null,
    @Json(name = "componentId")
    val componentId: String? = null
)
package ua.gov.diia.core.models.common_compose.mlc.chip

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ChipTimeMlc(
    @Json(name = "id")
    val id: String? = null,
    @Json(name = "label")
    val label: String? = null,
    @Json(name = "active")
    val active: Boolean? = null,
    @Json(name = "componentId")
    val componentId: String? = null,
    @Json(name = "dataJson")
    val data: String? = null,
)
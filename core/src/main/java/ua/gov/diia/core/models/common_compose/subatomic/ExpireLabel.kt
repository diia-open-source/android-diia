package ua.gov.diia.core.models.common_compose.subatomic

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ExpireLabel(
    @Json(name = "expireLabelFirst")
    val expireLabelFirst: String,
    @Json(name = "expireLabelLast")
    val expireLabelLast: String?,
    @Json(name = "timer")
    val timer: Int
)
package ua.gov.diia.core.models.common_compose.atm.icon


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class BadgeCounterAtm(
    @Json(name = "count")
    val count: Int
)
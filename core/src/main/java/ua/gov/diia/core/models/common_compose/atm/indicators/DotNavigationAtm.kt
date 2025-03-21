package ua.gov.diia.core.models.common_compose.atm.indicators


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class DotNavigationAtm(
    @Json(name = "count")
    val count: Int,
    @Json(name = "componentId")
    val componentId: String? = null
)
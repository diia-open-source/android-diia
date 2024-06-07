package ua.gov.diia.core.models.common_compose.atm.button

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CalendarItemAtm(
    @Json(name = "label")
    val label: String?,
    @Json(name = "isActive")
    val isActive: Boolean?,
    @Json(name = "isToday")
    val isToday: Boolean?,
    @Json(name = "isSelected")
    val isSelected: Boolean?,
)
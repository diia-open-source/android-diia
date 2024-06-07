package ua.gov.diia.core.models.common_compose.org.calendar

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import ua.gov.diia.core.models.common_compose.atm.button.CalendarItemAtm
import ua.gov.diia.core.models.common_compose.org.chip.ChipGroupOrg

@JsonClass(generateAdapter = true)
data class CalendarItemOrg(
    @Json(name = "date")
    val date: String,
    @Json(name = "componentId")
    val componentId: String?,
    @Json(name = "calendarItemAtm")
    val calendarItemAtm: CalendarItemAtm?,
    @Json(name = "chipGroupOrg")
    val chipGroupOrg: ChipGroupOrg?
)
package ua.gov.diia.ui_base.components.organism.calendar

import android.text.format.DateUtils.isToday
import ua.gov.diia.core.models.common_compose.org.calendar.CalendarItemOrg
import ua.gov.diia.core.util.DateFormats
import ua.gov.diia.ui_base.components.atom.button.CalendarItemAtmData
import ua.gov.diia.ui_base.components.infrastructure.state.UIState
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.organism.chip.ChipGroupOrgData
import ua.gov.diia.ui_base.components.organism.chip.toUiModel
import java.util.Calendar
import java.util.Calendar.DAY_OF_MONTH
import java.util.Date

data class CalendarItemOrgData(
    val date: String,
    val calendarItemAtm: CalendarItemAtmData?,
    val chipGroupOrg: ChipGroupOrgData?,
    val componentId: UiText? = null
)

fun CalendarItemOrg.toUiModel(): CalendarItemOrgData {
    val date = item?.date ?: run { return CalendarItemOrgData("", null, null) }
    val itemDate = try {
        DateFormats.uaDateFormat.parse(date)
    } catch (e: Exception) {
        Date()
    }
    val itemDay = Calendar.getInstance().apply {
        time = itemDate
    }
    val itemDayOfMonth = itemDay.get(DAY_OF_MONTH)

    return CalendarItemOrgData(
        date = date,
        calendarItemAtm = CalendarItemAtmData(
            title = itemDayOfMonth.toString(),
            interaction = UIState.Interaction.Enabled,
            isToday = isToday(itemDay.time.time)
        ),
        chipGroupOrg = item?.chipGroupOrg?.toUiModel(itemDayOfMonth.toString()),
        componentId = UiText.DynamicString(item?.componentId.orEmpty())
    )
}

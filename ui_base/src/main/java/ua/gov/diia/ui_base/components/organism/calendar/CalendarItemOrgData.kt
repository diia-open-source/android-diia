package ua.gov.diia.ui_base.components.organism.calendar

import ua.gov.diia.core.models.common_compose.org.calendar.CalendarItemOrg
import ua.gov.diia.ui_base.components.atom.button.CalendarItemAtmData
import ua.gov.diia.ui_base.components.atom.button.toUiModel
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.organism.chip.ChipGroupOrgData
import ua.gov.diia.ui_base.components.organism.chip.toUiModel

data class CalendarItemOrgData(
    val date: String,
    val calendarItemAtm: CalendarItemAtmData?,
    val chipGroupOrg: ChipGroupOrgData?,
    val componentId: UiText? = null
)

fun CalendarItemOrg.toUiModel(): CalendarItemOrgData {
    return CalendarItemOrgData(
        date = date,
        calendarItemAtm = calendarItemAtm?.toUiModel(),
        chipGroupOrg = chipGroupOrg?.toUiModel(calendarItemAtm?.label.orEmpty()),
        componentId = UiText.DynamicString(componentId.orEmpty())
    )
}

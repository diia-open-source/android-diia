package ua.gov.diia.ui_base.components.organism.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.core.models.common_compose.general.Action
import ua.gov.diia.core.models.common_compose.mlc.text.CurrentTimeMlc
import ua.gov.diia.core.models.common_compose.org.calendar.CalendarOrg
import ua.gov.diia.core.util.DateFormats
import ua.gov.diia.ui_base.components.DiiaResourceIcon
import ua.gov.diia.ui_base.components.atom.button.CalendarItemAtm
import ua.gov.diia.ui_base.components.atom.button.CalendarItemAtmData
import ua.gov.diia.ui_base.components.atom.divider.DividerSlimAtom
import ua.gov.diia.ui_base.components.atom.icon.IconAtm
import ua.gov.diia.ui_base.components.atom.icon.IconAtmData
import ua.gov.diia.ui_base.components.infrastructure.DataActionWrapper
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.state.UIState
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.molecule.chip.ChipTimeMlcData
import ua.gov.diia.ui_base.components.molecule.text.CurrentTimeMlc
import ua.gov.diia.ui_base.components.molecule.text.CurrentTimeMlcData
import ua.gov.diia.ui_base.components.molecule.text.toUiModel
import ua.gov.diia.ui_base.components.organism.chip.ChipGroupOrg
import ua.gov.diia.ui_base.components.organism.chip.ChipGroupOrgData
import ua.gov.diia.ui_base.components.subatomic.loader.TridentLoaderAtm
import ua.gov.diia.ui_base.components.theme.White
import ua.gov.diia.ui_base.util.toUiModel
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Composable
fun CalendarOrg(
    modifier: Modifier = Modifier,
    data: CalendarOrgData,
    onUIAction: (UIAction) -> Unit
) {
    val calendarInstance = remember { data.calendarDate }
    val calendarDays = remember { configureCalendarItemDatesGrid(calendarInstance, data) }
    val chips = remember {
        calendarDays.find { it.calendarItemAtm?.selection == UIState.Selection.Selected }?.chipGroupOrg
    }

    val isInProgress = data.progress == UIState.Progress.InProgress
    val showMonthPicker = data.expand == UIState.Expand.Expanded
    Column(modifier = modifier.fillMaxWidth()) {
        Box {
            Column(
                modifier = modifier
                    .padding(horizontal = 24.dp)
                    .padding(top = 24.dp)
                    .background(White, shape = RoundedCornerShape(8.dp))
                    .alpha(if (isInProgress) 0f else 1f)
            ) {
                CurrentTimeWithNavigationRow(modifier, data, onUIAction)

                DividerSlimAtom()

                if (!showMonthPicker) {
                    CalendarDaysGrid(modifier, data, calendarDays, onUIAction)
                } else {
                    CalendarMonthsGrid(modifier, data, onUIAction)
                }
            }

            if (isInProgress) {
                TridentLoaderAtm(Modifier.align(Alignment.Center))
            }
        }

        if (chips != null && !isInProgress && !showMonthPicker) {
            ChipGroupOrg(data = chips, onUIAction = onUIAction)
        }
    }
}

@Composable
private fun CurrentTimeWithNavigationRow(
    modifier: Modifier,
    data: CalendarOrgData,
    onUIAction: (UIAction) -> Unit
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        data.iconBack?.let {
            IconAtm(data = data.iconBack, onUIAction = onUIAction)
        }
        data.currentTime?.let {
            CurrentTimeMlc(data = data.currentTime, onUIAction = onUIAction)
        }
        data.iconForward?.let {
            IconAtm(data = data.iconForward, onUIAction = onUIAction)
        }
    }
}

@Composable
private fun CalendarDaysGrid(
    modifier: Modifier,
    data: CalendarOrgData,
    calendarDays: List<CalendarItemOrgData>,
    onUIAction: (UIAction) -> Unit
) {
    LazyVerticalGrid(
        modifier = modifier
            .padding(horizontal = 8.dp)
            .padding(top = 8.dp, bottom = 16.dp),
        columns = GridCells.Fixed(data.columnsAmount),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(calendarDays.size) { i ->
            val itemCalendar = calendarDays[i]
            if (itemCalendar.calendarItemAtm != null) {
                CalendarItemAtm(
                    data = itemCalendar.calendarItemAtm,
                    onUIAction = onUIAction
                )
            }
        }
    }
}

@Composable
private fun CalendarMonthsGrid(
    modifier: Modifier = Modifier,
    data: CalendarOrgData,
    onUIAction: (UIAction) -> Unit
) {
    LazyVerticalGrid(
        modifier = modifier
            .padding(horizontal = 8.dp)
            .padding(top = 8.dp, bottom = 16.dp),
        columns = GridCells.Fixed(3),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(data.months.size) { i ->
            val month = data.months[i]
            CalendarItemAtm(
                data = CalendarItemAtmData(
                    actionKey = UIActionKeysCompose.CALENDAR_ITEM_MONTH_ATM,
                    title = month,
                    interaction = UIState.Interaction.Enabled,
                ),
                onUIAction = onUIAction
            )
        }
    }
}

data class CalendarOrgData(
    val actionKey: String = UIActionKeysCompose.CALENDAR_ORG,
    val calendarDate: Calendar,
    val iconBack: IconAtmData? = null,
    val iconForward: IconAtmData? = null,
    val currentTime: CurrentTimeMlcData? = null,
    val componentId: UiText? = null,
    val columnsAmount: Int,
    val items: List<CalendarItemOrgData>? = null,
    val progress: UIState.Progress = UIState.Progress.Downloaded,
    val expand: UIState.Expand = UIState.Expand.Collapsed
) {

    val weekName = listOf("пн", "вт", "ср", "чт", "пт", "сб", "нд")

    val months =
        listOf("Січ", "Лют", "Бер", "Кві", "Тра", "Чер", "Лип", "Сер", "Вер", "Жов", "Лис", "Гру")

    fun selectDay(day: String): CalendarOrgData {
        val result = mutableListOf<CalendarItemOrgData>()
        items?.forEach {
            val e = if (it.calendarItemAtm?.title == day) {
                it.copy(calendarItemAtm = it.calendarItemAtm.copy(selection = UIState.Selection.Selected))
            } else {
                val item = it.calendarItemAtm?.copy(selection = UIState.Selection.Unselected)
                val chipTimeItems = it.chipGroupOrg?.chipTimeItems?.unselectAll()
                val group = it.chipGroupOrg?.copy(chipTimeItems = chipTimeItems)
                it.copy(calendarItemAtm = item, chipGroupOrg = group)
            }
            result.add(e)
        }
        return copy(items = result)
    }

    fun selectChip(code: String, day: String): CalendarOrgData {
        val result = mutableListOf<CalendarItemOrgData>()
        if (items.isNullOrEmpty()) return this
        items.forEach {
            val correctDay = it.calendarItemAtm?.title == day
            val chipTime = mutableListOf<ChipTimeMlcData>()
            it.chipGroupOrg?.chipTimeItems?.forEach { chipMlc ->
                val chip = if (correctDay && chipMlc.code == code) {
                    chipMlc.copy(selection = UIState.Selection.Selected)
                } else {
                    chipMlc.copy(selection = UIState.Selection.Unselected)
                }
                chipTime.add(chip)
            }

            val group = it.chipGroupOrg?.copy(chipTimeItems = chipTime)
            result.add(it.copy(chipGroupOrg = group))
        }

        return copy(items = result)
    }

    private fun List<ChipTimeMlcData>?.unselectAll(): List<ChipTimeMlcData> {
        return this?.map { c ->
            c.copy(selection = UIState.Selection.Unselected)
        }.orEmpty()
    }
}

fun CalendarOrg.toUiModel(): CalendarOrgData {
    return CalendarOrgData(
        calendarDate = this.currentTimeMlc.toCalendarInstance(),
        iconBack = this.iconForMovingBackwards?.iconAtm?.toUiModel(),
        iconForward = this.iconForMovingForward?.iconAtm?.toUiModel(),
        currentTime = this.currentTimeMlc?.toUiModel(),
        componentId = UiText.DynamicString(this.componentId.orEmpty()),
        columnsAmount = this.columnsAmount ?: 7,
        items = this.items?.map { it.toUiModel() }
    )
}

private fun configureCalendarItemDatesGrid(
    calendarInstance: Calendar,
    data: CalendarOrgData
): List<CalendarItemOrgData> {
    val lastDayOfMonth: Int = calendarInstance.getActualMaximum(Calendar.DAY_OF_MONTH)
    val days = mutableListOf<CalendarItemOrgData>()
    val daysAvailable = data.items.orEmpty()

    //Add all days for current month, if date was received from BE, inject that date
    for (i in 1..lastDayOfMonth) {
        val availableDay =
            daysAvailable.find { it.calendarItemAtm?.title?.toIntOrNull() == i }
        if (availableDay != null) {
            days.add(availableDay)
        } else {
            days.add(
                CalendarItemOrgData(
                    date = "",
                    chipGroupOrg = null,
                    calendarItemAtm = CalendarItemAtmData(title = "$i")
                )
            )
        }
    }

    //add empty placeholders before first day of week
    //For instance, if first day is Wednesday, two empty place should be added
    val daysAddToFirstWeek = calculateDaysToAddToFirstWeek(
        calendarInstance.get(Calendar.DAY_OF_WEEK)
    )
    for (i in 1..daysAddToFirstWeek) {
        val item = CalendarItemOrgData(
            date = "",
            chipGroupOrg = null,
            calendarItemAtm = CalendarItemAtmData(title = "")
        )
        days.add(0, item)
    }

    //Add week day names to top of the calendar grid
    data.weekName.reversed().forEach {
        val item = CalendarItemOrgData(
            date = "",
            chipGroupOrg = null,
            calendarItemAtm = CalendarItemAtmData(title = it)
        )
        days.add(0, item)
    }
    return days
}

private fun CurrentTimeMlc?.toCalendarInstance(): Calendar {
    val month = this?.action?.resource ?: DateFormats.calendarMonthFormat.format(Date())
    val c = Calendar.getInstance(Locale("UK"))
    val date = try {
        DateFormats.calendarMonthFormat.parse(month)
    } catch (e: Exception) {
        Date()
    }
    c.time = date
    return c
}


/**
 *   Метод для вираховування к-сть пустих днів, які необхідно додати перед першим днем місяця
 *   Так як день під номером 1 SUNDAY, а для нашого регіону MONDAY, тому віднімаємо 2
 */
private fun calculateDaysToAddToFirstWeek(dayOfWeek: Int): Int {
    return when (dayOfWeek) {
        Calendar.SUNDAY -> 6
        else -> dayOfWeek - 2
    }
}

@Deprecated(
    message = "fun for test purpose, use CalendarOrg instead", replaceWith = ReplaceWith(
        "CalendarOrg(modifier, dataState.value, onUIAction)"
    )
)
@Composable
fun CalendarOrgReComposable(
    modifier: Modifier = Modifier,
    dataState: State<CalendarOrgData>,
    onUIAction: (UIAction) -> Unit
) {
    key(dataState.value) {
        CalendarOrg(modifier, dataState.value, onUIAction)
    }
}

@Composable
@Preview
fun CalendarOrgPreview() {
    val chipTimeItems = mutableListOf<ChipTimeMlcData>()
    for (i in 0..10) {
        val data = ChipTimeMlcData(
            title = UiText.DynamicString("hour " + i),
            selection = UIState.Selection.Unselected,
            id = "id $i",
            code = "code $i",
            resourceId = " resourceId : $i",
            date = "27"
        )
        chipTimeItems.add(data)
    }

    val chips = ChipGroupOrgData(
        label = UiText.DynamicString("Оберіть час:"),
        chipTimeItems = chipTimeItems,
    )

    val chipTimeItems2 = mutableListOf<ChipTimeMlcData>()
    for (i in 0..4) {
        val data = ChipTimeMlcData(
            title = UiText.DynamicString("hour " + i),
            selection = UIState.Selection.Unselected,
            id = "idS $i",
            code = "codeS $i",
            resourceId = " resourceIdS : $i",
            date = "25"
        )
        chipTimeItems2.add(data)
    }

    val chips2 = ChipGroupOrgData(
        label = UiText.DynamicString("Оберіть час2:"),
        chipTimeItems = chipTimeItems2,
    )

    val item1 = CalendarItemOrgData(
        date = "25.02.2024",
        calendarItemAtm = CalendarItemAtmData(
            title = "25",
            interaction = UIState.Interaction.Enabled
        ),
        chipGroupOrg = chips2
    )


    val item2 = CalendarItemOrgData(
        date = "27.02.2024",
        calendarItemAtm = CalendarItemAtmData(
            title = "27",
            interaction = UIState.Interaction.Enabled,
            isToday = true
        ),
        chipGroupOrg = chips
    )

    val item3 = CalendarItemOrgData(
        date = "7.02.2024",
        calendarItemAtm = CalendarItemAtmData(
            title = "7",
            interaction = UIState.Interaction.Enabled,
            selection = UIState.Selection.Selected
        ),
        chipGroupOrg = ChipGroupOrgData(
            label = UiText.DynamicString("Оберіть час3:"),
            chipTimeItems = chipTimeItems.subList(0, 3).map { it.copy(date = "7") },
        )
    )

    val currentTime = CurrentTimeMlcData(
        label = UiText.DynamicString("label"),
        date = "02.2024"
    )
    val data = CalendarOrgData(
        iconBack = IconAtmData(
            code = DiiaResourceIcon.ARROW_MIN_LEFT.code,
            action = DataActionWrapper(type = "nextMonthData", resource = "01.2024")
        ),
        iconForward = IconAtmData(
            code = DiiaResourceIcon.ARROW_MIN_RIGHT.code,
            action = DataActionWrapper(type = "nextMonthData", resource = "03.2024")
        ),
        currentTime = CurrentTimeMlcData(
            label = UiText.DynamicString("Лютий 2024"),
            date = "02.2024"
        ),
        columnsAmount = 7,
        items = listOf(item1, item2, item3),
        progress = UIState.Progress.Downloaded,
        expand = UIState.Expand.Collapsed,
        calendarDate = CurrentTimeMlc(
            action = Action(resource = "02.24", type = "", subtype = "", subresource = "")
        ).toCalendarInstance()
    )


    val snapShot = remember { mutableStateOf(data) }

    CalendarOrgReComposable(Modifier, snapShot) {
        if (it.actionKey == "calendarItemAtm") {
            val day = it.data ?: throw Exception("day null")
            snapShot.value = snapShot.value.selectDay(day)
        } else if (it.actionKey == "chipTimeMlc") {
            val code = it.data ?: throw Exception("code null")
            val day = it.optionalId ?: throw Exception("day null")
            snapShot.value = snapShot.value.selectChip(code, day)
        } else if (it.actionKey == "iconAtm") {
            if (snapShot.value.expand == UIState.Expand.Collapsed) {
                val nextDate = it.action?.resource
                //DO request with nextDate to api/v1/public-service/marriage/application/{applicationId}/calendar-slots?startDate={string <mm.yyyy>}
                //add progress update

                //here
                val cal = CurrentTimeMlc(
                    action = Action(resource = nextDate, type = "", subtype = "", subresource = "")
                ).toCalendarInstance()
                snapShot.value = snapShot.value.copy(calendarDate = cal)
            } else {
                //update year by 1 or reduce by 1 and update CurrentTime label
            }
        } else if (it.actionKey == "currentTimeMlc") {
            val year = snapShot.value.calendarDate.get(Calendar.YEAR).toString()
            snapShot.value = snapShot.value.copy(
                currentTime = currentTime.copy(label = UiText.DynamicString(year)),
                expand = UIState.Expand.Expanded
            )
        } else if (it.actionKey == "calendarItemMonthAtm") {
            val monthIndex = snapShot.value.months.indexOf(it.data)
            val cal = snapShot.value.calendarDate.apply {
                set(Calendar.MONTH, monthIndex)
            }
            snapShot.value = snapShot.value.copy(calendarDate = cal)
            //DO request api/v1/public-service/marriage/application/{applicationId}/calendar-slots?startDate={string <mm.yyyy>}
            //add progress update
        }
    }
}
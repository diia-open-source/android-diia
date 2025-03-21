package ua.gov.diia.ui_base.components.organism.calendar

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.core.models.common_compose.general.Action
import ua.gov.diia.core.models.common_compose.mlc.text.CurrentTimeMlc
import ua.gov.diia.core.models.common_compose.org.calendar.CalendarOrg
import ua.gov.diia.core.util.DateFormats
import ua.gov.diia.ui_base.R
import ua.gov.diia.ui_base.components.DiiaResourceIcon
import ua.gov.diia.ui_base.components.atom.button.CalendarItemAtm
import ua.gov.diia.ui_base.components.atom.button.CalendarItemAtmData
import ua.gov.diia.ui_base.components.atom.divider.DividerSlimAtom
import ua.gov.diia.ui_base.components.atom.icon.IconAtm
import ua.gov.diia.ui_base.components.atom.icon.IconAtmData
import ua.gov.diia.ui_base.components.atom.text.textwithparameter.TextWithParametersData
import ua.gov.diia.ui_base.components.infrastructure.DataActionWrapper
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose.CALENDAR_ITEM_BACKWARD
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose.CALENDAR_ITEM_FORWARD
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose.CALENDAR_ITEM_REFRESH_CALENDAR
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose.CALENDAR_ITEM_UPDATE_DATE
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose.CURRENT_TIME_MLC
import ua.gov.diia.ui_base.components.infrastructure.state.UIState
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.molecule.chip.ChipTimeMlcData
import ua.gov.diia.ui_base.components.molecule.message.StubMessageMlc
import ua.gov.diia.ui_base.components.molecule.message.StubMessageMlcData
import ua.gov.diia.ui_base.components.molecule.message.toUIModel
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
import kotlin.math.ceil
import kotlin.math.roundToInt

private const val MONTHS_COLUMN_SIZE = 3

@Composable
fun CalendarOrg(
    modifier: Modifier = Modifier,
    data: CalendarOrgData,
    progressIndicator: Pair<String, Boolean> = Pair("", false),
    onUIAction: (UIAction) -> Unit
) {
    var calendarInstance = data.calendarDate
    val calendarDays = configureCalendarItemDatesGrid(calendarInstance, data)
    val chips = calendarDays.find {
        it.calendarItemAtm?.selection == UIState.Selection.Selected
    }?.chipGroupOrg
    val isInProgress = data.actionKey == progressIndicator.first && progressIndicator.second
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
                CurrentTimeWithNavigationRow(modifier, data, showMonthPicker) {
                    if (isInProgress) return@CurrentTimeWithNavigationRow
                    if (it.actionKey == CURRENT_TIME_MLC) {
                        onUIAction.invoke(it)
                    } else {

                        if (!showMonthPicker && it.action?.resource != null) {
                            calendarInstance = toCalendarInstance(it.action.resource)
                        } else {
                            calendarInstance.add(
                                Calendar.MONTH,
                                monthsToChange(it.actionKey, showMonthPicker)
                            )
                        }

                        if (showMonthPicker) {
                            onUIAction(
                                UIAction(
                                    actionKey = CALENDAR_ITEM_REFRESH_CALENDAR,
                                    data = calendarInstance.get(Calendar.YEAR).toString()
                                )
                            )
                        } else {
                            val date = DateFormats.calendarMonthFormat.format(calendarInstance.time)
                            onUIAction(UIAction(actionKey = CALENDAR_ITEM_UPDATE_DATE, data = date))
                        }
                    }
                }

                DividerSlimAtom()
                when {
                    showMonthPicker -> CalendarMonthsGrid(modifier, data, onUIAction)
                    data.items.isNullOrEmpty() && data.stubMessageMlcData != null -> {
                        StubMessageMlc(
                            modifier = Modifier
                                .background(
                                    Color.White,
                                    shape = RoundedCornerShape(bottomEnd = 8.dp, bottomStart = 8.dp)
                                )
                                .padding(bottom = 64.dp),
                            data = data.stubMessageMlcData,
                            onUIAction = {}
                        )
                    }

                    else -> CalendarDaysGrid(modifier, data, calendarDays, onUIAction)
                }
            }

            if (isInProgress) {
                TridentLoaderAtm(Modifier.align(Alignment.Center))
            }
        }

        if (chips != null
            && (!chips.chipTimeItems.isNullOrEmpty() || !chips.chipMoleculeItems.isNullOrEmpty())
            && !isInProgress
            && !showMonthPicker
        ) {
            ChipGroupOrg(data = chips, onUIAction = onUIAction)

            //Scroll animation to chips, for now disabled
//            LaunchedEffect(key1 = chips.chipTimeItems) {
//                coroutineScope {
//                    scroll.animateScrollTo(scroll.maxValue)
//                }
//            }
        } else {
            if (!isInProgress && !showMonthPicker) {
                StubMessageMlc(
                    data = StubMessageMlcData(
                        icon = UiText.StringResource(R.string.calendar_org_stub_msg_icon),
                        title = UiText.StringResource(R.string.calendar_org_stub_msg_title),
                    ),
                    modifier = Modifier.padding(bottom = 64.dp),
                    onUIAction = onUIAction
                )
            }
        }
    }
}

@Composable
private fun CurrentTimeWithNavigationRow(
    modifier: Modifier,
    data: CalendarOrgData,
    showMonthPicker: Boolean,
    onUIAction: (UIAction) -> Unit
) {
    val year = data.calendarDate.get(Calendar.YEAR).toString()
    val label = if (showMonthPicker) year else data.currentTime?.label?.asString().orEmpty()
    val currentTimeData = data.currentTime?.copy(label = UiText.DynamicString(label))

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        data.iconBack?.let {
            IconAtm(
                data = data.iconBack.copy(
                    interactionState = getBackIconState(data, showMonthPicker)
                ),
                onUIAction = onUIAction
            )
        }
        currentTimeData?.let {
            CurrentTimeMlc(data = currentTimeData, onUIAction = onUIAction)
        }
        data.iconForward?.let {
            IconAtm(
                data = data.iconForward.copy(
                    interactionState = getForwardIconState(data, showMonthPicker)
                ),
                onUIAction = onUIAction
            )
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

    val heightDp = 40.dp * ceil((calendarDays.size.toDouble() / data.columnsAmount)).toInt().inc()
    LazyVerticalGrid(
        modifier = modifier
            .padding(horizontal = 8.dp)
            .padding(top = 8.dp, bottom = 16.dp)
            .height(heightDp + 8.dp),
        columns = GridCells.Fixed(data.columnsAmount),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(calendarDays.size) { i ->
            val itemCalendar = calendarDays[i]
            if (itemCalendar.calendarItemAtm != null) {
                CalendarItemAtm(
                    modifier = Modifier,
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
    val heightDp = 40.dp * (data.months.size.toDouble() / MONTHS_COLUMN_SIZE).roundToInt().inc()

    val isCurrentYear = data.calendarDate.get(Calendar.YEAR) == data.currentDate.get(Calendar.YEAR)
    val currentMonthIndex = data.currentDate.get(Calendar.MONTH)

    val isMaxDateYear = data.maxDate?.let {
        data.calendarDate.get(Calendar.YEAR) == data.maxDate.get(Calendar.YEAR)
    } ?: false
    val maxMonthIndex = data.maxDate?.get(Calendar.MONTH) ?: 0

    LazyVerticalGrid(
        modifier = modifier
            .padding(top = 8.dp, bottom = 16.dp)
            .height(heightDp),
        columns = GridCells.Fixed(MONTHS_COLUMN_SIZE),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        items(data.months.size) { i ->
            val month = data.months[i]
            val isMonthBeforeToday = isCurrentYear && currentMonthIndex > i
            val isMonthAfterMaxDate = isMaxDateYear && maxMonthIndex < i
            val interactionState = if (isMonthBeforeToday || isMonthAfterMaxDate) {
                UIState.Interaction.Disabled
            } else {
                UIState.Interaction.Enabled
            }
            CalendarItemAtm(
                modifier = Modifier
                    .width(100.dp)
                    .padding(start = 12.dp),
                data = CalendarItemAtmData(title = month, interaction = interactionState),
                onUIAction = {
                    val monthIndex = data.months.indexOf(it.data)
                    val cal = data.calendarDate.apply {
                        set(Calendar.MONTH, monthIndex)
                    }
                    val date = DateFormats.calendarMonthFormat.format(cal.time)
                    onUIAction(
                        UIAction(
                            actionKey = CALENDAR_ITEM_UPDATE_DATE,
                            data = date,
                            optionalId = "collapse"
                        )
                    )
                }
            )
        }
    }
}

fun getBackIconState(data: CalendarOrgData, showMonthPicker: Boolean): UIState.Interaction {
    return if (data.calendarDate.get(Calendar.YEAR) == data.currentDate.get(Calendar.YEAR)) {
        if (showMonthPicker) {
            UIState.Interaction.Disabled
        } else {
            data.iconBack?.interactionState ?: UIState.Interaction.Enabled
        }
    } else {
        UIState.Interaction.Enabled
    }
}

fun getForwardIconState(data: CalendarOrgData, showMonthPicker: Boolean): UIState.Interaction {
    return if (showMonthPicker) {
        if (data.maxDate != null) {
            val sameYear = data.calendarDate.get(Calendar.YEAR) == data.maxDate.get(Calendar.YEAR)
            if (sameYear) UIState.Interaction.Disabled else UIState.Interaction.Enabled
        } else {
            UIState.Interaction.Enabled
        }
    } else {
        data.iconForward?.interactionState ?: UIState.Interaction.Enabled
    }
}

data class CalendarOrgData(
    val actionKey: String = UIActionKeysCompose.CALENDAR_ORG,
    val calendarDate: Calendar,
    val currentDate: Calendar,
    val maxDate: Calendar? = null,
    val iconBack: IconAtmData? = null,
    val iconForward: IconAtmData? = null,
    val currentTime: CurrentTimeMlcData? = null,
    val componentId: UiText? = null,
    val columnsAmount: Int,
    val items: List<CalendarItemOrgData>? = null,
    val stubMessageMlcData: StubMessageMlcData? = null,
    val selectedChip: ChipTimeMlcData? = null,
    val expand: UIState.Expand = UIState.Expand.Collapsed
) : UIElementData {

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
        return copy(items = result, selectedChip = null)
    }

    fun selectChip(code: String, dayOfMonth: String): CalendarOrgData {
        val result = mutableListOf<CalendarItemOrgData>()
        if (items.isNullOrEmpty()) return this
        var selectedChip: ChipTimeMlcData? = null
        items.forEach {
            val correctDay = it.calendarItemAtm?.title == dayOfMonth
            val chipTime = mutableListOf<ChipTimeMlcData>()
            it.chipGroupOrg?.chipTimeItems?.forEach { chipMlc ->
                val chip = if (correctDay && chipMlc.id == code) {
                    selectedChip = chipMlc
                    chipMlc.copy(selection = UIState.Selection.Selected)
                } else {
                    chipMlc.copy(selection = UIState.Selection.Unselected)
                }
                chipTime.add(chip)
            }

            val group = it.chipGroupOrg?.copy(chipTimeItems = chipTime)
            result.add(it.copy(chipGroupOrg = group))
        }

        return copy(items = result, selectedChip = selectedChip)
    }

    private fun List<ChipTimeMlcData>?.unselectAll(): List<ChipTimeMlcData> {
        return this?.map { c ->
            c.copy(selection = UIState.Selection.Unselected)
        }.orEmpty()
    }
}

fun CalendarOrg.toUiModel(): CalendarOrgData {
    return CalendarOrgData(
        calendarDate = toCalendarInstance(currentTimeMlc?.action?.resource),
        currentDate = Calendar.getInstance(),
        maxDate = if (currentTimeMlc?.maxDate != null) toCalendarInstance(currentTimeMlc?.maxDate) else null,
        iconBack = this.iconForMovingBackwards?.iconAtm?.toUiModel(actionKey = CALENDAR_ITEM_BACKWARD),
        iconForward = this.iconForMovingForward?.iconAtm?.toUiModel(actionKey = CALENDAR_ITEM_FORWARD),
        currentTime = this.currentTimeMlc?.toUiModel(),
        componentId = UiText.DynamicString(this.componentId.orEmpty()),
        columnsAmount = this.columnsAmount ?: 7,
        items = this.items?.map { it.toUiModel() },
        stubMessageMlcData = this.stubMessageMlc?.toUIModel()
    )
}

private fun configureCalendarItemDatesGrid(
    calendarInstance: Calendar,
    data: CalendarOrgData
): SnapshotStateList<CalendarItemOrgData> {
    val lastDayOfMonth: Int = calendarInstance.getActualMaximum(Calendar.DAY_OF_MONTH)
    val days = mutableListOf<CalendarItemOrgData>()
    val daysAvailable = data.items.orEmpty()
    val isCurrentMonth =
        (calendarInstance.get(Calendar.YEAR) == data.currentDate.get(Calendar.YEAR)) &&
                (calendarInstance.get(Calendar.MONTH) == data.currentDate.get(Calendar.MONTH))
    val currentDay = data.currentDate.get(Calendar.DAY_OF_MONTH)

    //Add all days for current month, if date was received from BE, inject that date
    for (i in 1..lastDayOfMonth) {
        val isToday = (isCurrentMonth && currentDay == i)
        val availableDay = daysAvailable.find { it.calendarItemAtm?.title?.toIntOrNull() == i }
        val item = availableDay?.calendarItemAtm?.copy(isToday = isToday)
        if (availableDay != null) {
            days.add(availableDay.copy(calendarItemAtm = item))
        } else {
            days.add(
                CalendarItemOrgData(
                    date = "",
                    chipGroupOrg = null,
                    calendarItemAtm = CalendarItemAtmData(title = "$i", isToday = isToday)
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
    return SnapshotStateList<CalendarItemOrgData>().apply { addAll(days) }
}

private fun toCalendarInstance(resource: String?): Calendar {
    val month = resource ?: DateFormats.calendarMonthFormat.format(Date())
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

private fun monthsToChange(
    actionKey: String,
    showMonthPicker: Boolean,
): Int {
    return if (actionKey == CALENDAR_ITEM_FORWARD) {
        if (showMonthPicker) 12 else 1
    } else if (actionKey == CALENDAR_ITEM_BACKWARD) {
        if (showMonthPicker) -12 else -1
    } else 0
}

@Deprecated(
    message = "fun for test purpose, use CalendarOrg instead",
    replaceWith = ReplaceWith("CalendarOrg(modifier, dataState.value, onUIAction)")
)
@Composable
fun CalendarOrgReComposable(
    modifier: Modifier = Modifier,
    dataState: State<CalendarOrgData>,
    onUIAction: (UIAction) -> Unit
) {
    key(dataState.value) {
        CalendarOrg(modifier, dataState.value, Pair("", false), onUIAction)
    }
}

private fun buildPreviewData(): CalendarOrgData {
    val chipTimeItems = mutableListOf<ChipTimeMlcData>()
    for (i in 0..10) {
        val data = ChipTimeMlcData(
            title = UiText.DynamicString("hour " + i),
            selection = UIState.Selection.Unselected,
            id = "id $i",
            dataJson = "code $i",
            dayOfMonth = "27"
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
            dataJson = "code $i",
            dayOfMonth = "26"
        )
        chipTimeItems2.add(data)
    }

    val chips2 = ChipGroupOrgData(
        label = UiText.DynamicString("Оберіть час2:"),
        chipTimeItems = chipTimeItems2,
    )

    val item1 = CalendarItemOrgData(
        date = "26.02.2024",
        calendarItemAtm = CalendarItemAtmData(
            title = "26",
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
            chipTimeItems = chipTimeItems.subList(0, 3).map { it.copy(dayOfMonth = "7") },
        )
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
        expand = UIState.Expand.Collapsed,
        calendarDate = toCalendarInstance(
            CurrentTimeMlc(
                action = Action(resource = "02.24", type = "", subtype = "", subresource = "")
            ).action?.resource
        ),
        currentDate = Calendar.getInstance(),
        maxDate = toCalendarInstance("02.2026")
    )
    return data
}

@Composable
@Preview
fun CalendarOrgPreviewNoSlots() {
    val currentTime = CurrentTimeMlcData(
        label = UiText.DynamicString("label"),
        date = "02.2024"
    )
    val data = buildPreviewData()
    val newData = data.copy(
        items = listOf(), stubMessageMlcData = StubMessageMlcData(
            icon = UiText.DynamicString("\uD83D\uDC4C"),
            title = UiText.DynamicString("Дати недоступні"),
            description = TextWithParametersData(
                text = UiText.DynamicString("Дата церемонії має бути не пізніше, ніж завершується термін дії паспортів нареченого та нареченої.")
            )
        )
    )
    val snapShot = remember { mutableStateOf(newData) }

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
                val cal = toCalendarInstance(
                    CurrentTimeMlc(
                        action = Action(
                            resource = nextDate,
                            type = "",
                            subtype = "",
                            subresource = ""
                        )
                    ).action?.resource
                )
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
        } else if (it.actionKey == "calendarItemUpdateDate") {
            val date = it.data ?: throw Exception("day null")
            snapShot.value = snapShot.value.copy(
                expand = UIState.Expand.Collapsed
            )
        }
    }
}

@Composable
@Preview
fun CalendarOrgPreview() {
    val currentTime = CurrentTimeMlcData(
        label = UiText.DynamicString("label"),
        date = "02.2024"
    )
    val data = buildPreviewData()
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
                val cal = toCalendarInstance(
                    CurrentTimeMlc(
                        action = Action(
                            resource = nextDate,
                            type = "",
                            subtype = "",
                            subresource = ""
                        )
                    ).action?.resource
                )
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
        }
    }
}

@Composable
@Preview
fun CalendarOrgPreview_months() {
    val chipTimeItems = mutableListOf<ChipTimeMlcData>()
    for (i in 0..10) {
        val data = ChipTimeMlcData(
            title = UiText.DynamicString("hour " + i),
            selection = UIState.Selection.Unselected,
            id = "id $i",
            dataJson = "code $i",
            dayOfMonth = "27"
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
            dataJson = "code $i",
            dayOfMonth = "26"
        )
        chipTimeItems2.add(data)
    }

    val chips2 = ChipGroupOrgData(
        label = UiText.DynamicString("Оберіть час2:"),
        chipTimeItems = chipTimeItems2,
    )

    val item1 = CalendarItemOrgData(
        date = "26.02.2024",
        calendarItemAtm = CalendarItemAtmData(
            title = "26",
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
            chipTimeItems = chipTimeItems.subList(0, 3).map { it.copy(dayOfMonth = "7") },
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
        expand = UIState.Expand.Expanded,
        calendarDate = toCalendarInstance(
            CurrentTimeMlc(
                action = Action(resource = "02.24", type = "", subtype = "", subresource = "")
            ).action?.resource
        ),
        currentDate = Calendar.getInstance(),
        maxDate = toCalendarInstance("02.2026"),
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
                val cal = toCalendarInstance(
                    CurrentTimeMlc(
                        action = Action(
                            resource = nextDate,
                            type = "",
                            subtype = "",
                            subresource = ""
                        )
                    ).action?.resource
                )
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
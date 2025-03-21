package ua.gov.diia.ui_base.components.molecule.input

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.core.models.common_compose.mlc.input.InputDateMlc
import ua.gov.diia.core.util.extensions.date_time.getLocalDate
import ua.gov.diia.core.util.extensions.date_time.getLocalTime
import ua.gov.diia.core.util.extensions.date_time.parseFromISO8601ToCalendar
import ua.gov.diia.core.util.extensions.date_time.parseToISO8601
import ua.gov.diia.ui_base.R
import ua.gov.diia.ui_base.components.atom.divider.DividerSlimAtom
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.toDynamicString
import ua.gov.diia.ui_base.components.theme.Black
import ua.gov.diia.ui_base.components.theme.BlackAlpha30
import ua.gov.diia.ui_base.components.theme.DiiaTextStyle
import ua.gov.diia.ui_base.components.theme.White
import java.util.Calendar

typealias ApiDateFormat = ua.gov.diia.core.models.common_compose.mlc.input.DateFormat

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputDateMlc(
    modifier: Modifier = Modifier,
    data: InputDateMlcData,
    onUIAction: (UIAction) -> Unit
) {
    val context = LocalContext.current
    val datePickerState = rememberDatePickerState()
    val showDatePicker = remember { mutableStateOf(false) }

    val minDate = remember { mutableStateOf<Calendar?>(null) }
    val maxDate = remember { mutableStateOf<Calendar?>(null) }

    val hintAsString = data.hint?.asString()

    LaunchedEffect(Unit) {
        data.minDate?.let {
            try {
                minDate.value = parseFromISO8601ToCalendar(it)
            } catch (e: Exception) {
                //TODO send error to crashlytics
            }
        }
        data.maxDate?.let {
            try {
                maxDate.value = parseFromISO8601ToCalendar(it)
            } catch (e: Exception) {
                //TODO send error to crashlytics
            }
        }
    }


    Column(modifier = modifier
        .fillMaxWidth()
        .clickable {
            showDatePicker.value = true
        }) {
        data.label?.let {
            Text(
                text = data.label.asString(),
                style = DiiaTextStyle.t4TextSmallDescription,
                color = if (data.isEnabled) Black else BlackAlpha30
            )
        }
        Spacer(modifier = Modifier.height(6.dp))
        Row(
            modifier = Modifier,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (data.value != null) {
                val calendar = parseFromISO8601ToCalendar(data.value)
                val displayDate = when (data.dateFormat) {
                    DateFormat.MONTH_AND_YEAR -> String.format(
                        "%02d / %02d",
                        calendar.get(Calendar.DAY_OF_MONTH),
                        calendar.get(Calendar.MONTH) + 1 //TODO Need check

                    )

                    DateFormat.FULL, null -> String.format(
                        "%02d / %02d / %04d",
                        calendar.get(Calendar.DAY_OF_MONTH),
                        calendar.get(Calendar.MONTH) + 1,
                        calendar.get(Calendar.YEAR)
                    )
                }
                Text(
                    text = displayDate,
                    style = DiiaTextStyle.t1BigText,
                    color = Black
                )
            } else {
                data.placeholder?.let {
                    Text(
                        text = it.asString(),
                        style = DiiaTextStyle.t1BigText,
                        color = BlackAlpha30
                    )
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                modifier = Modifier.size(width = 16.dp, height = 16.dp),
                painter = painterResource(R.drawable.diia_icon_calendar),
                contentDescription = stringResource(R.string.select_date)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))

        DividerSlimAtom(modifier = Modifier.height(2.dp), color = BlackAlpha30)

        Spacer(modifier = Modifier.height(8.dp))

        data.hint?.let {
            Text(
                modifier = Modifier.fillMaxWidth(),
                style = DiiaTextStyle.t4TextSmallDescription,
                text = it.asString(),
                color = BlackAlpha30
            )
        }

        if (showDatePicker.value) {
            DatePickerDialog(
                onDismissRequest = {
                    showDatePicker.value = false
                },
                confirmButton = {
                    TextButton(
                        modifier = Modifier.background(White),
                        colors = ButtonDefaults.buttonColors(
                            contentColor = Black,
                            containerColor = Color.Transparent
                        ),
                        onClick = {
                            datePickerState.selectedDateMillis?.let {
                                val isValid = isSelectedDateInRange(
                                    selectedDateMillis = it,
                                    minDate = minDate.value,
                                    maxDate = maxDate.value
                                )
                                if (isValid) {
                                    datePickerState.selectedDateMillis?.let {
                                        showDatePicker.value = false
                                    }
                                    val localDate = getLocalDate(it)
                                    val localTime = getLocalTime()
                                    val serverSendFormat = parseToISO8601(localDate, localTime)
                                    onUIAction(
                                        UIAction(
                                            actionKey = data.actionKey,
                                            optionalId = data.id,
                                            data = serverSendFormat
                                        )
                                    )
                                } else {
                                    data.hint?.let {
                                        Toast.makeText(
                                            context,
                                            hintAsString,
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }

                                }
                            }
                        }
                    ) {
                        Text(
                            UiText.StringResource(R.string.date_time_picker_confirm_button_label)
                                .asString()
                        )
                    }
                },
                dismissButton = {
                    TextButton(
                        modifier = Modifier.background(White),
                        colors = ButtonDefaults.buttonColors(
                            contentColor = Black,
                            containerColor = Color.Transparent
                        ),
                        onClick = {
                            showDatePicker.value = false
                        }
                    ) {
                        Text(
                            UiText.StringResource(R.string.date_time_picker_cancel_button_label)
                                .asString()
                        )
                    }
                },
                colors = DatePickerDefaults.colors(
                    containerColor = White,
                )
            ) {
                DatePicker(
                    modifier = Modifier.background(White),
                    state = datePickerState,
                    colors = DatePickerDefaults.colors(
                        containerColor = White,
                        selectedDayContainerColor = Black,
                        todayDateBorderColor = Black,
                        todayContentColor = Black,
                        yearContentColor = Black,
                        currentYearContentColor = Black,
                        selectedYearContentColor = White,
                        selectedYearContainerColor = Black,
                    )
                )
            }
        }
    }
}

private fun isSelectedDateInRange(
    selectedDateMillis: Long,
    minDate: Calendar?,
    maxDate: Calendar?
): Boolean {
    if (minDate != null && maxDate == null) {
        return minDate.setBeginningOfTheDay().timeInMillis <= selectedDateMillis
    }
    if (minDate == null && maxDate != null) {
        return maxDate.setBeginningOfTheDay().timeInMillis >= selectedDateMillis
    }
    if (minDate != null && maxDate != null) {
        return minDate.setBeginningOfTheDay().timeInMillis >= selectedDateMillis
                &&
                maxDate.setBeginningOfTheDay().timeInMillis <= selectedDateMillis
    }
    return true
}

private fun Calendar.setBeginningOfTheDay(): Calendar {
    this.apply {
        set(Calendar.HOUR, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
    }
    return this
}

fun InputDateMlc.toUIModel(
    id: String? = null,
    minDate: String? = null,
    maxDate: String? = null,
): InputDateMlcData {
    return InputDateMlcData(
        id = this.id ?: id,
        componentId = this.componentId?.let { UiText.DynamicString(it) },
        placeholder = this.placeholder.toDynamicString(),
        label = this.label.toDynamicString(),
        hint = this.hint?.toDynamicString(),
        value = this.value,
        dateFormat = when (this.dateFormat) {
            ApiDateFormat.full -> DateFormat.FULL
            ApiDateFormat.monthAndYear,
            null -> DateFormat.MONTH_AND_YEAR
        },
        minDate = minDate,
        maxDate = maxDate,
        mandatory = this.mandatory
    )
}


data class InputDateMlcData(
    val actionKey: String = UIActionKeysCompose.INPUT_DATE_MLC,
    val id: String? = null,
    val componentId: UiText? = null,
    val placeholder: UiText? = null,
    val label: UiText? = null,
    val hint: UiText? = null,
    val isEnabled: Boolean = true,
    val value: String? = null,
    val dateFormat: DateFormat? = null,
    val minDate: String? = null,
    val maxDate: String? = null,
    val mandatory: Boolean? = null
) : InputFormItem() {

    fun onInputChanged(newValue: String?): InputDateMlcData {
        if (newValue.isNullOrEmpty()) return this
        return this.copy(
            value = newValue
        )
    }
}

enum class DateFormat {
    FULL, //дд / мм / рррр
    MONTH_AND_YEAR //мм/рррр
}

fun InputDateMlc.toUIModel(
    minDate: String? = null,
    maxDate: String? = null,
): InputDateMlcData {
    return InputDateMlcData(
        componentId = this.componentId.orEmpty().toDynamicString(),
        id = this.id,
        placeholder = this.placeholder?.toDynamicString(),
        label = this.label.let {
            it.toDynamicString()
        },
        value = this.value,
        hint = this.hint?.let {
            it.toDynamicString()
        },
        isEnabled = true,
        dateFormat = when (this.dateFormat) {
            ApiDateFormat.full -> DateFormat.FULL
            ApiDateFormat.monthAndYear,
            null -> DateFormat.MONTH_AND_YEAR
        },
        minDate = minDate,
        maxDate = maxDate,
        mandatory = this.mandatory
    )
}

@Composable
@Preview
fun InputDateMlcPreview() {
    val data = InputDateMlcData(
        placeholder = "placeholder".toDynamicString(),
        label = "label".toDynamicString(),
        hint = "hint".toDynamicString(),
        dateFormat = DateFormat.FULL,
        minDate = "2024-05-26T07:37:57.911Z"
    )
    val state = remember { mutableStateOf(data) }

    Column(modifier = Modifier.fillMaxSize()) {
        InputDateMlc(
            data = state.value
        ) {
            state.value = state.value.copy(
                value = it.data
            )
        }
    }
}

@Composable
@Preview
fun InputDateMlcPreview_Month_And_Year() {
    val data = InputDateMlcData(
        placeholder = "placeholder".toDynamicString(),
        label = "label".toDynamicString(),
        hint = "hint".toDynamicString(),
        dateFormat = DateFormat.MONTH_AND_YEAR,
        minDate = "2024-05-26T07:37:57.911Z"
    )
    val state = remember { mutableStateOf(data) }

    Column(modifier = Modifier.fillMaxSize()) {
        InputDateMlc(
            data = state.value
        ) {
            state.value = state.value.onInputChanged(it.data)
        }
    }
}

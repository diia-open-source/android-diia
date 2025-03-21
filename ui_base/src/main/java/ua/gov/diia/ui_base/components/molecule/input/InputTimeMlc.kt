package ua.gov.diia.ui_base.components.molecule.input

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerDefaults
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import ua.gov.diia.core.models.common_compose.mlc.input.InputTimeMlc
import ua.gov.diia.core.models.common_compose.mlc.input.TimeFormat
import ua.gov.diia.core.util.extensions.date_time.getLocalDate
import ua.gov.diia.core.util.extensions.date_time.parseFromISO8601ToCalendar
import ua.gov.diia.core.util.extensions.date_time.parseToISO8601
import ua.gov.diia.core.util.extensions.date_time.toLocalTime
import ua.gov.diia.ui_base.R
import ua.gov.diia.ui_base.components.atom.divider.DividerSlimAtom
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.toDynamicString
import ua.gov.diia.ui_base.components.theme.Black
import ua.gov.diia.ui_base.components.theme.BlackAlpha10
import ua.gov.diia.ui_base.components.theme.BlackAlpha30
import ua.gov.diia.ui_base.components.theme.BlackSqueeze
import ua.gov.diia.ui_base.components.theme.DiiaTextStyle
import ua.gov.diia.ui_base.components.theme.White
import java.util.Calendar
import java.util.concurrent.TimeUnit


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputTimeMlc(
    modifier: Modifier = Modifier,
    data: InputTimeMlcData,
    onUIAction: (UIAction) -> Unit
) {
    val context = LocalContext.current
    val timePickerState = rememberTimePickerState(is24Hour = true)
    val showTimePicker = remember { mutableStateOf(false) }

    val minTimeInMillis = remember { mutableStateOf<Long?>(null) }
    val maxTimeInMillis = remember { mutableStateOf<Long?>(null) }

    val hintMessage = data.hint?.asString()

    LaunchedEffect(Unit) {
        parseFromISO8601ToCalendar(data.minDate).let { cal ->
            val millisecondOfDay: Long = (
                    TimeUnit.HOURS.toMillis(cal.get(Calendar.HOUR_OF_DAY).toLong()) +
                            TimeUnit.MINUTES.toMillis(cal.get(Calendar.MINUTE).toLong()) +
                            TimeUnit.SECONDS.toMillis(cal.get(Calendar.SECOND).toLong()) +
                            cal.get(Calendar.MILLISECOND))
            minTimeInMillis.value = millisecondOfDay
        }
        parseFromISO8601ToCalendar(data.maxDate).let { cal ->
            val millisecondOfDay: Long = (
                    TimeUnit.HOURS.toMillis(cal.get(Calendar.HOUR_OF_DAY).toLong()) +
                            TimeUnit.MINUTES.toMillis(cal.get(Calendar.MINUTE).toLong()) +
                            TimeUnit.SECONDS.toMillis(cal.get(Calendar.SECOND).toLong()) +
                            cal.get(Calendar.MILLISECOND))
            maxTimeInMillis.value = millisecondOfDay
        }
    }

    Column(modifier = modifier
        .fillMaxWidth()
        .clickable {
            showTimePicker.value = true
        }) {
        data.label?.let {
            Text(
                text = data.label.asString(),
                style = DiiaTextStyle.t4TextSmallDescription,
                color = if (data.isEnabled) Black else BlackAlpha30
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (data.value != null) {
                val calendar = parseFromISO8601ToCalendar(data.value)
                val displayTime = when (data.dateFormat) {
                    InputTimeMlcTimeFormat.FULL -> String.format(
                        "%02d : %02d : %02d",
                        calendar.get(Calendar.HOUR_OF_DAY),
                        calendar.get(Calendar.MINUTE),
                        calendar.get(Calendar.SECOND),
                    )

                    null,
                    InputTimeMlcTimeFormat.HOUR_AND_MINUTE -> String.format(
                        "%02d : %02d",
                        calendar.get(Calendar.HOUR_OF_DAY),
                        calendar.get(Calendar.MINUTE)
                    )
                }
                Text(
                    text = displayTime,
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
                painter = painterResource(R.drawable.diia_icon_clock),
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

        if (showTimePicker.value) {
            TimePickerDialog(
                label = data.label,
                onDismissRequest = {
                    showTimePicker.value = false
                },
                confirmButton = {
                    TextButton(
                        colors = ButtonDefaults.buttonColors(
                            contentColor = Black,
                            containerColor = Color.Transparent
                        ),
                        onClick = {
                            val selectedTimeInMillis =
                                TimeUnit.HOURS.toMillis(timePickerState.hour.toLong()) +
                                        TimeUnit.MINUTES.toMillis(timePickerState.minute.toLong())

                            val isValid = isSelectedTimeInRange(
                                selectedTimeMillis = selectedTimeInMillis,
                                minTime = minTimeInMillis.value,
                                maxTime = maxTimeInMillis.value
                            )
                            val localDate = getLocalDate(selectedTimeInMillis)
                            val localTime =
                                toLocalTime(timePickerState.hour, timePickerState.minute)
                            if (isValid) {
                                showTimePicker.value = false
                                val serverSendFormat = parseToISO8601(localDate, localTime)
                                onUIAction(
                                    UIAction(
                                        actionKey = data.actionKey,
                                        data = serverSendFormat
                                    )
                                )
                            } else {
                                hintMessage?.let {
                                    Toast.makeText(
                                        context,
                                        it,
                                        Toast.LENGTH_SHORT
                                    ).show()
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
                        colors = ButtonDefaults.buttonColors(
                            contentColor = Black,
                            containerColor = Color.Transparent
                        ),
                        onClick = {
                            showTimePicker.value = false
                        }
                    ) {
                        Text(
                            UiText.StringResource(R.string.date_time_picker_cancel_button_label)
                                .asString()
                        )
                    }
                },
                containerColor = White
            ) {
                TimePicker(
                    state = timePickerState,
                    colors = TimePickerDefaults.colors(
                        timeSelectorSelectedContainerColor = BlackSqueeze,
                        timeSelectorUnselectedContainerColor = BlackAlpha10,
                        timeSelectorSelectedContentColor = Black,
                        timeSelectorUnselectedContentColor = Black,
                        clockDialColor = White,
                        clockDialUnselectedContentColor = Black,
                        clockDialSelectedContentColor = White,
                        selectorColor = Black
                    )
                )
            }
        }
    }
}

data class InputTimeMlcData(
    val actionKey: String = UIActionKeysCompose.INPUT_TIME_MLC,
    val componentId: UiText? = null,
    val placeholder: UiText? = null,
    val label: UiText? = null,
    val value: String? = null,
    val hint: UiText? = null,
    val isEnabled: Boolean = true,
    val dateFormat: InputTimeMlcTimeFormat? = null,
    val minDate: String? = null,
    val maxDate: String? = null,
    val mandatory: Boolean
) : UIElementData {
    fun onInputChanged(newValue: String?): InputTimeMlcData {
        if (newValue.isNullOrEmpty()) return this
        return this.copy(
            value = newValue
        )
    }
}

fun InputTimeMlc.toUIModel(
    minDate: String? = null,
    maxDate: String? = null,
): InputTimeMlcData {
    return InputTimeMlcData(
        componentId = this.componentId.orEmpty().toDynamicString(),
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
            TimeFormat.full -> InputTimeMlcTimeFormat.FULL
            TimeFormat.HourAndMinute,
            null -> InputTimeMlcTimeFormat.HOUR_AND_MINUTE
        },
        minDate = minDate,
        maxDate = maxDate,
        mandatory = this.mandatory
    )
}

enum class InputTimeMlcTimeFormat {
    FULL,
    HOUR_AND_MINUTE
}

private fun isSelectedTimeInRange(
    selectedTimeMillis: Long,
    minTime: Long?,
    maxTime: Long?
): Boolean {
    if (minTime != null && maxTime == null) {
        return minTime <= selectedTimeMillis
    }
    if (minTime == null && maxTime != null) {
        return maxTime >= selectedTimeMillis
    }
    if (minTime != null && maxTime != null) {
        return selectedTimeMillis in minTime..maxTime
    }
    return true
}

@Composable
@Preview
fun InputTimeMlcPreview() {
    val data = InputTimeMlcData(
        placeholder = "00 : 00".toDynamicString(),
        label = "Виберіть зручний для вас час".toDynamicString(),
        value = "2024-05-26T07:30:00.000Z",
        hint = "Робочі години: з 9:00 до 18:00".toDynamicString(),
        minDate = "2024-05-26T06:00:00.000Z",
        maxDate = "2024-05-26T15:00:00.000Z",
        isEnabled = true,
        mandatory = false
    )
    val state = remember { mutableStateOf(data) }


    Column(modifier = Modifier.fillMaxSize()) {
        InputTimeMlc(
            data = state.value
        ) {
            state.value = state.value.onInputChanged(it.data)
        }
    }
}

@Composable
fun TimePickerDialog(
    label: UiText?,
    onDismissRequest: () -> Unit,
    confirmButton: @Composable (() -> Unit),
    dismissButton: @Composable (() -> Unit)? = null,
    containerColor: Color = MaterialTheme.colorScheme.surface,
    content: @Composable () -> Unit,
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        ),
    ) {
        Surface(
            shape = MaterialTheme.shapes.extraLarge,
            tonalElevation = 6.dp,
            modifier = Modifier
                .width(IntrinsicSize.Min)
                .height(IntrinsicSize.Min)
                .background(
                    shape = MaterialTheme.shapes.extraLarge,
                    color = containerColor
                ),
            color = containerColor
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                label?.let {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 20.dp),
                        text = it.asString(),
                        style = MaterialTheme.typography.labelMedium
                    )
                }
                content()
                Row(
                    modifier = Modifier
                        .height(40.dp)
                        .fillMaxWidth()
                ) {
                    Spacer(modifier = Modifier.weight(1f))
                    dismissButton?.invoke()
                    confirmButton()
                }
            }
        }
    }
}

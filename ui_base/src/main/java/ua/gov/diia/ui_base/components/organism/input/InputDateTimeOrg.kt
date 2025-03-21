package ua.gov.diia.ui_base.components.organism.input

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.core.models.common_compose.org.input.InputDateTimeOrg
import ua.gov.diia.ui_base.components.infrastructure.DataActionWrapper
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.toDynamicString
import ua.gov.diia.ui_base.components.molecule.input.InputDateMlc
import ua.gov.diia.ui_base.components.molecule.input.InputDateMlcData
import ua.gov.diia.ui_base.components.molecule.input.InputTimeMlc
import ua.gov.diia.ui_base.components.molecule.input.InputTimeMlcData
import ua.gov.diia.ui_base.components.molecule.input.toUIModel

@Composable
fun InputDateTimeOrg(
    modifier: Modifier = Modifier,
    data: InputDateTimeOrgData,
    onUIAction: (UIAction) -> Unit
) {
    val currentSelectedDate = remember {
        mutableStateOf<String?>(null)
    }
    val currentSelectedTime = remember {
        mutableStateOf<String?>(null)
    }

    Column(modifier = modifier.fillMaxWidth()) {
        data.inputDateMlc?.let {
            InputDateMlc(
                data = data.inputDateMlc,
                onUIAction = {
                    currentSelectedDate.value = it.data
                    onUIAction(
                        UIAction(
                            actionKey = data.actionKey,
                            action = DataActionWrapper(
                                type = UIActionKeysCompose.INPUT_DATE_TIME_ORG,
                                subtype = UIActionKeysCompose.INPUT_DATE_MLC,
                                resource = formatResult(
                                    currentSelectedDate.value,
                                    currentSelectedTime.value
                                )
                            )
                        )
                    )
                }
            )
        }
        data.inputTimeMlc?.let {
            Spacer(modifier = Modifier.height(16.dp))
            InputTimeMlc(
                data = data.inputTimeMlc,
                onUIAction = {
                    currentSelectedTime.value = it.data
                    onUIAction(
                        UIAction(
                            actionKey = data.actionKey,
                            action = DataActionWrapper(
                                type = UIActionKeysCompose.INPUT_DATE_TIME_ORG,
                                subtype = UIActionKeysCompose.INPUT_TIME_MLC,
                                resource = formatResult(
                                    currentSelectedDate.value,
                                    currentSelectedTime.value
                                )
                            )
                        )
                    )
                }
            )
        }
    }
}

private fun formatResult(selectedDate: String?, selectedTime: String?): String? {
    if (selectedDate != null && selectedTime == null) {
        return selectedDate
    }
    if (selectedDate == null && selectedTime != null) {
        return selectedTime
    }
    if (selectedDate != null && selectedTime != null) {
        return try {
            val date = selectedDate.substring(0, 11)
            val time = selectedTime.substring(11, selectedTime.length)
            val result = date + time
            result
        } catch (e: Exception) {
            null
        }
    }
    return null
}

data class InputDateTimeOrgData(
    val actionKey: String = UIActionKeysCompose.INPUT_DATE_TIME_ORG,
    val componentId: UiText? = null,
    val maxDate: String? = null,
    val minDate: String? = null,
    val inputCode: String? = null,
    val inputDateMlc: InputDateMlcData?,
    val inputTimeMlc: InputTimeMlcData?,
) : UIElementData {
    fun onInputChanged(subtype: String?, newValue: String?): InputDateTimeOrgData {
        if (newValue.isNullOrEmpty() || subtype.isNullOrEmpty()) return this
        return this.copy(
            inputDateMlc = if (subtype == UIActionKeysCompose.INPUT_DATE_MLC && inputDateMlc != null) {
                this.inputDateMlc.copy(
                    value = newValue
                )
            } else {
                this.inputDateMlc
            },
            inputTimeMlc = if (subtype == UIActionKeysCompose.INPUT_TIME_MLC && inputTimeMlc != null) {
                this.inputTimeMlc.copy(
                    value = newValue
                )
            } else {
                this.inputTimeMlc
            }
        )
    }
}

fun InputDateTimeOrg.toUIModel(): InputDateTimeOrgData {
    return InputDateTimeOrgData(
        componentId = componentId.orEmpty().toDynamicString(),
        inputCode = inputCode,
        inputDateMlc = inputDateMlc?.toUIModel(minDate, maxDate),
        minDate = minDate,
        maxDate = maxDate,
        inputTimeMlc = inputTimeMlc?.toUIModel(minDate, maxDate)
    )
}


@Preview
@Composable
fun InputDateTimeOrg() {
    val minDate = "2024-05-24T06:00:00.000Z"
    val maxDate = "2024-05-26T15:00:00.000Z"
    val data = InputDateTimeOrgData(
        minDate = minDate,
        maxDate = maxDate,
        inputDateMlc = InputDateMlcData(
            placeholder = "placeholder".toDynamicString(),
            label = "label".toDynamicString(),
            hint = "hint".toDynamicString(),
            minDate = "2024-05-26T07:37:57.911Z"
        ),
        inputTimeMlc = InputTimeMlcData(
            placeholder = "00 : 00".toDynamicString(),
            label = "Виберіть зручний для вас час".toDynamicString(),
            value = "2024-05-26T07:30:00.000Z",
            hint = "Робочі години: з 9:00 до 18:00".toDynamicString(),
            minDate = "2024-05-26T06:00:00.000Z",
            maxDate = "2024-05-26T15:00:00.000Z",
            mandatory = false,
            isEnabled = true
        )
    )
    val state = remember { mutableStateOf(data) }

    InputDateTimeOrg(
        data = state.value
    ) {
        state.value =
            state.value.onInputChanged(subtype = it.action?.subtype, newValue = it.action?.resource)
    }
}

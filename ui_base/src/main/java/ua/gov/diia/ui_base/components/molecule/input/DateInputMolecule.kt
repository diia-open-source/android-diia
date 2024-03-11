package ua.gov.diia.ui_base.components.molecule.input

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.ui_base.R
import ua.gov.diia.ui_base.components.atom.divider.DividerSlimAtom
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.state.UIState
import ua.gov.diia.ui_base.components.theme.Black
import ua.gov.diia.ui_base.components.theme.BlackAlpha30
import ua.gov.diia.ui_base.components.theme.DiiaTextStyle
import ua.gov.diia.ui_base.components.theme.Red

@Composable
fun DateInputMolecule(
    modifier: Modifier = Modifier,
    data: DateInputMoleculeData,
    onUIAction: (UIAction) -> Unit
) {
    Column(modifier = modifier
        .fillMaxWidth()
        .clickable {
            onUIAction(UIAction(actionKey = UIActionKeysCompose.DATE_INPUT, optionalId = data.id))
        }) {
        data.label?.let {
            Text(
                text = data.label,
                style = DiiaTextStyle.t4TextSmallDescription,
                color = when (data.interactionState) {
                    UIState.Interaction.Enabled -> Black
                    UIState.Interaction.Disabled -> BlackAlpha30
                }
            )
        }
        val color = if (data.inputValue.isNullOrEmpty() && data.validationState != UIState.Validation.Failed) {
            BlackAlpha30
        } else if (!data.inputValue.isNullOrEmpty() && data.validationState == UIState.Validation.Failed) {
            Red
        } else {
            Black
        }
        Spacer(modifier = Modifier.height(6.dp))
        Row(
            modifier = Modifier,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = getValueForIndex(LocalContext.current, DateSection.DD, data.inputValue),
                style = DiiaTextStyle.t1BigText,
                color = color
            )
            Text(
                "/",
                style = DiiaTextStyle.t1BigText,
                color = color
            )
            Text(
                text = getValueForIndex(LocalContext.current, DateSection.MM, data.inputValue),
                style = DiiaTextStyle.t1BigText,
                color = color
            )
            Text(
                "/",
                style = DiiaTextStyle.t1BigText,
                color = color
            )
            Text(
                text = getValueForIndex(LocalContext.current, DateSection.YYYY, data.inputValue),
                style = DiiaTextStyle.t1BigText,
                color = color
            )
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                modifier = Modifier.size(width = 16.dp, height = 16.dp),
                painter = painterResource(R.drawable.diia_icon_calendar),
                contentDescription = stringResource(R.string.select_date)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))

        DividerSlimAtom(modifier = Modifier.height(2.dp), color = color)

        Spacer(modifier = Modifier.height(8.dp))

        data.hintMessage?.let {
            if (data.validationState != UIState.Validation.Failed) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    style = DiiaTextStyle.t4TextSmallDescription,
                    text = it,
                    color = BlackAlpha30
                )
            }
        }

        data.errorMessage?.let {
            if (data.validationState == UIState.Validation.Failed) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    style = DiiaTextStyle.t4TextSmallDescription,
                    text = it,
                    color = color
                )
            }
        }
    }
}

private fun getValueForIndex(context: Context, section: DateSection, inputValue: String?): String {
    if (inputValue.isNullOrEmpty()) {
        return when (section) {
            DateSection.DD -> context.resources.getString(R.string.day_placeholder)
            DateSection.MM -> context.resources.getString(R.string.month_placeholder)
            DateSection.YYYY -> context.resources.getString(R.string.year_placeholder)
        }
    } else {
        val resultArray = inputValue.split(".")
        return if (resultArray.size == 3) {
            if (section.index <= resultArray.size - 1) {
                resultArray[section.index]
            } else {
                ""
            }
        } else {
            ""
        }
    }
}

private enum class DateSection(val index: Int) {
    DD(0), MM(1), YYYY(2)
}

data class DateInputMoleculeData(
    val actionKey: String = UIActionKeysCompose.DATE_INPUT,
    val id: String? = null,
    val label: String? = null,
    val inputValue: String? = null,
    val hintMessage: String? = null,
    val errorMessage: String? = null,
    val interactionState: UIState.Interaction = UIState.Interaction.Enabled,
    val validationState: UIState.Validation = UIState.Validation.NeverBeenPerformed
) : UIElementData {

    fun onInputChanged(newValue: String?): DateInputMoleculeData {
        if (newValue == null) return this
        return this.copy(
            inputValue = newValue,
            validationState = UIState.Validation.Passed
        )
    }
}

@Composable
@Preview
fun DateInputMoleculePreview_Empty() {
    val data = DateInputMoleculeData(
        actionKey = UIActionKeysCompose.DATE_INPUT,
        id = null,
        label = "Label",
        inputValue = null,
        hintMessage = "Hint message",
        errorMessage = "Error message",
        interactionState = UIState.Interaction.Enabled,
        validationState = UIState.Validation.NeverBeenPerformed
    )

    DateInputMolecule(modifier = Modifier.padding(16.dp), data = data) {

    }
}

@Composable
@Preview
fun DateInputMoleculePreview_Hint() {
    val data = DateInputMoleculeData(
        actionKey = UIActionKeysCompose.DATE_INPUT,
        id = null,
        label = "Label",
        inputValue = null,
        hintMessage = "Hint message",
        errorMessage = "Error message",
        interactionState = UIState.Interaction.Enabled,
        validationState = UIState.Validation.NeverBeenPerformed
    )

    DateInputMolecule(modifier = Modifier.padding(16.dp), data = data) {

    }
}

@Composable
@Preview
fun DateInputMoleculePreview_Disabled() {
    val data = DateInputMoleculeData(
        actionKey = UIActionKeysCompose.DATE_INPUT,
        id = null,
        label = "Label",
        inputValue = null,
        hintMessage = "Hint message",
        errorMessage = "Error message",
        interactionState = UIState.Interaction.Disabled,
        validationState = UIState.Validation.NeverBeenPerformed
    )

    DateInputMolecule(modifier = Modifier.padding(16.dp), data = data) {

    }
}

@Composable
@Preview
fun DateInputMoleculePreview_Filled() {
    val data = DateInputMoleculeData(
        actionKey = UIActionKeysCompose.DATE_INPUT,
        id = null,
        label = "Label",
        inputValue = "27.12.1993",
        hintMessage = "Hint message",
        errorMessage = "Error message",
        interactionState = UIState.Interaction.Enabled,
        validationState = UIState.Validation.NeverBeenPerformed
    )

    DateInputMolecule(modifier = Modifier.padding(16.dp), data = data) {

    }
}

@Composable
@Preview
fun DateInputMoleculePreview_Error() {
    val data = DateInputMoleculeData(
        actionKey = UIActionKeysCompose.DATE_INPUT,
        id = null,
        label = "Label",
        inputValue = "27.12.1993",
        hintMessage = "Hint message",
        errorMessage = "Error message",
        interactionState = UIState.Interaction.Enabled,
        validationState = UIState.Validation.Failed
    )

    DateInputMolecule(modifier = Modifier.padding(16.dp), data = data) {

    }
}
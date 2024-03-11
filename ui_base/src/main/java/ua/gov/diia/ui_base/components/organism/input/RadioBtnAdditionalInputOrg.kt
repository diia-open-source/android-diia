package ua.gov.diia.ui_base.components.organism.input

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.ui_base.components.atom.radio.RadioBtnAtm
import ua.gov.diia.ui_base.components.atom.radio.RadioBtnAtmData
import ua.gov.diia.ui_base.components.atom.radio.RadioButtonMode
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.state.UIState
import ua.gov.diia.ui_base.components.molecule.input.TextInputMolecule
import ua.gov.diia.ui_base.components.molecule.input.TextInputMoleculeData
import ua.gov.diia.ui_base.components.theme.White

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun RadioBtnAdditionalInputOrg(
    modifier: Modifier = Modifier,
    data: RadioBtnAdditionalInputOrgData,
    shape: RoundedCornerShape = RoundedCornerShape(bottomStart = 8.dp, bottomEnd = 8.dp),
    onUIAction: (UIAction) -> Unit
) {
    val showTextInput = data.radioBtnAtmData.selectionState == UIState.Selection.Selected

    Column(
        modifier = modifier
            .background(
                color = White,
                shape = shape
            )
            .padding(16.dp)

    ) {
        RadioBtnAtm(
            data = data.radioBtnAtmData,
            onUIAction = {
                onUIAction(it)
            }
        )
        if (showTextInput) {
            TextInputMolecule(
                modifier = Modifier.padding(top = 16.dp),
                data = data.inputTextMlcData,
                onUIAction = {
                    onUIAction(it)
                }
            )
        }
    }
}

data class RadioBtnAdditionalInputOrgData(
    val radioBtnAtmData: RadioBtnAtmData,
    val inputTextMlcData: TextInputMoleculeData
) : UIElementData {

    fun onRadioButtonClick(): RadioBtnAdditionalInputOrgData {
        return this.copy(radioBtnAtmData = radioBtnAtmData.onRadioButtonClick())
    }

    fun onInputChanged(newValue: String?): RadioBtnAdditionalInputOrgData {
        if (newValue == null) return this
        return this.copy(inputTextMlcData = inputTextMlcData.onInputChanged(newValue))
    }

    fun dropSelection() : RadioBtnAdditionalInputOrgData{
        return this.copy(radioBtnAtmData = radioBtnAtmData.copy(selectionState = UIState.Selection.Unselected))
    }
}

@Composable
@Preview
fun RadioBtnAdditionalInputOrgPreview() {
    val radioBtnAtmData = RadioBtnAtmData(
        id = "1",
        label = "Label",
        mode = RadioButtonMode.SINGLE_CHOICE,
        interactionState = UIState.Interaction.Enabled,
        selectionState = UIState.Selection.Selected,
        status = "Status"
    )

    val inputTextMlcData = TextInputMoleculeData(
        id = "",
        label = "label",
        placeholder = "Placeholder",
        keyboardType = KeyboardType.Number,
        validation = UIState.Validation.NeverBeenPerformed
    )


    val radioBtnAdditionalInputOrgData = RadioBtnAdditionalInputOrgData(
        radioBtnAtmData = radioBtnAtmData,
        inputTextMlcData = inputTextMlcData
    )

    RadioBtnAdditionalInputOrg(
        modifier = Modifier.fillMaxWidth(),
        data = radioBtnAdditionalInputOrgData,
    ) { }

}
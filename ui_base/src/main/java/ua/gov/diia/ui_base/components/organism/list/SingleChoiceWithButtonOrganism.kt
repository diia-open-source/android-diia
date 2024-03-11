package ua.gov.diia.ui_base.components.organism.list

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import ua.gov.diia.ui_base.components.atom.button.ButtonIconAtom
import ua.gov.diia.ui_base.components.atom.button.ButtonIconAtomData
import ua.gov.diia.ui_base.components.atom.checkbox.CheckboxCircleAtomData
import ua.gov.diia.ui_base.components.atom.checkbox.CheckboxMode
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.state.UIState
import ua.gov.diia.ui_base.components.molecule.list.checkbox.CheckboxMolecule
import ua.gov.diia.ui_base.components.molecule.list.checkbox.CheckboxMoleculeData

@Composable
fun SingleChoiceWithButtonOrganism(
    modifier: Modifier = Modifier,
    data: SingleChoiceWithButtonOrganismData,
    onUIAction: (UIAction) -> Unit
) {
    Column(modifier = modifier) {
        CheckboxMolecule(
            data = data.radioData,
            onUIAction = onUIAction
        )
        data.buttonData?.also { buttonData ->
            Spacer(modifier = Modifier.height(16.dp))
            ButtonIconAtom(
                modifier = Modifier.fillMaxWidth(),
                data = buttonData,
                onUIAction = onUIAction
            )
        }
    }
}

data class SingleChoiceWithButtonOrganismData(
    val actionKey: String = UIActionKeysCompose.SINGLE_CHOICE_WITH_BUTTON_ORGANISM,
    val radioData: CheckboxMoleculeData,
    val buttonData: ButtonIconAtomData?,
) : UIElementData {

    fun mapSelected(selectedId: String): SingleChoiceWithButtonOrganismData {
        return copy(radioData = radioData.mapSelected(CheckboxMode.SINGLE_CHOICE, selectedId))
    }

    fun findSelectedItemId(): String? {
        return radioData.options.find {
            it.selectionState == UIState.Selection.Selected && it.interactionState == UIState.Interaction.Enabled
        }?.id
    }
}

@SuppressLint("UnrememberedMutableState")
@Preview
@Composable
private fun SingleChoiceWithButtonOrganismPreview() {
    SingleChoiceWithButtonOrganism(
        data = SingleChoiceWithButtonOrganismData(
            radioData = CheckboxMoleculeData(
                title = LoremIpsum(3).values.joinToString(),
                expandable = true,
                options = mutableStateListOf(
                    CheckboxCircleAtomData(
                        id = "1",
                        title = LoremIpsum(3).values.joinToString(),
                        interactionState = UIState.Interaction.Enabled,
                        selectionState = UIState.Selection.Unselected,
                    ),
                    CheckboxCircleAtomData(
                        id = "2",
                        title = LoremIpsum(3).values.joinToString(),
                        description = LoremIpsum(1).values.joinToString(),
                        interactionState = UIState.Interaction.Enabled,
                        selectionState = UIState.Selection.Selected,
                        status = LoremIpsum(1).values.joinToString()
                    ),
                    CheckboxCircleAtomData(
                        id = "3",
                        title = LoremIpsum(3).values.joinToString(),
                        description = LoremIpsum(2).values.joinToString(),
                        interactionState = UIState.Interaction.Disabled,
                        selectionState = UIState.Selection.Unselected,
                        status = LoremIpsum(1).values.joinToString()
                    ),
                    CheckboxCircleAtomData(
                        id = "3",
                        title = LoremIpsum(10).values.joinToString(),
                        description = LoremIpsum(10).values.joinToString(),
                        interactionState = UIState.Interaction.Disabled,
                        selectionState = UIState.Selection.Selected,
                        status = LoremIpsum(1).values.joinToString()
                    )
                )
            ),
            buttonData = ButtonIconAtomData(
                id = "",
                title = LoremIpsum(9).values.joinToString(),
                interactionState = UIState.Interaction.Enabled
            )
        ),
        onUIAction = {}
    )
}
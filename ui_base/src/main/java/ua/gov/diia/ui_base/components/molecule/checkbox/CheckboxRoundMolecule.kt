package ua.gov.diia.ui_base.components.molecule.checkbox

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.ui_base.R
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.state.UIState
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.theme.Black
import ua.gov.diia.ui_base.components.theme.BlackAlpha30
import ua.gov.diia.ui_base.components.theme.DiiaTextStyle
import ua.gov.diia.ui_base.components.theme.Mantis
import ua.gov.diia.ui_base.components.theme.White


@Composable
fun CheckboxRoundMolecule(
    modifier: Modifier = Modifier,
    data: CheckboxRoundMoleculeData,
    onUIAction: (UIAction) -> Unit
) {
    val onClick = {
        if (data.interactionState != UIState.Interaction.Disabled) {
            onUIAction(
                UIAction(actionKey = data.actionKey, data = data.id)
            )
        }
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable { onClick.invoke() },
        verticalAlignment = Alignment.Top
    ) {
        val bgModifier = when (data.interactionState) {
            UIState.Interaction.Enabled -> {
                when (data.selectionState) {
                    UIState.Selection.Selected -> Modifier.background(
                        color = Mantis,
                        shape = CircleShape
                    )

                    UIState.Selection.Unselected -> Modifier.border(
                        width = 2.dp,
                        color = Black,
                        shape = CircleShape
                    )
                }
            }

            UIState.Interaction.Disabled -> {
                when (data.selectionState) {
                    UIState.Selection.Selected -> Modifier.background(
                        color = BlackAlpha30,
                        shape = CircleShape
                    )

                    UIState.Selection.Unselected -> Modifier.border(
                        width = 2.dp,
                        color = BlackAlpha30,
                        shape = CircleShape
                    )
                }
            }
        }

        Box(
            modifier = Modifier
                .size(20.dp)
                .then(bgModifier),
            contentAlignment = Alignment.Center
        ) {
            if (data.selectionState == UIState.Selection.Selected) {
                Icon(
                    modifier = Modifier.size(10.dp, 8.dp),
                    painter = painterResource(id = R.drawable.diia_check),
                    contentDescription = null,
                    tint = White
                )
            }
        }

        Column(
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp)
                .weight(1f)
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 2.dp),
                text = data.title.asString(),
                style = DiiaTextStyle.t3TextBody,
                color = Black
            )

            data.description?.let {
                Text(
                    modifier = Modifier
                        .fillMaxWidth(),
                    text = data.description.asString(),
                    style = DiiaTextStyle.t2TextDescription,
                    color = BlackAlpha30
                )

            }
        }
        data.status?.let {
            Text(
                text = data.status.asString(),
                style = DiiaTextStyle.t3TextBody,
                color = BlackAlpha30
            )
        }
    }
}


data class CheckboxRoundMoleculeData(
    val actionKey: String = UIActionKeysCompose.RADIO_BUTTON,
    val id: String,
    val title: UiText,
    val description: UiText? = null,
    val status: UiText? = null,
    val selectionState: UIState.Selection = UIState.Selection.Unselected,
    val interactionState: UIState.Interaction = UIState.Interaction.Enabled,
) : UIElementData, Cloneable {

    public override fun clone(): CheckboxRoundMoleculeData {
        return super.clone() as CheckboxRoundMoleculeData
    }

    fun onRadioButtonClick(): CheckboxRoundMoleculeData {
        return this.copy(selectionState = this.selectionState.reverse())
    }
}


@Composable
@Preview
fun CheckboxRoundMoleculePreview() {
    val data = CheckboxRoundMoleculeData(
        id = "",
        title = UiText.DynamicString("Title"),
        description = UiText.DynamicString("Description"),
        status = UiText.DynamicString("status")
    )

    val state = remember {
        mutableStateOf(data)
    }

    CheckboxRoundMolecule(modifier = Modifier, data = state.value) {
        state.value = state.value.onRadioButtonClick()
    }
}

@Composable
@Preview
fun CheckboxRoundMoleculePreview_selected() {
    val data = CheckboxRoundMoleculeData(
        id = "",
        title = UiText.DynamicString("Title"),
        description = UiText.DynamicString("Description"),
        status = UiText.DynamicString("status"),
        selectionState = UIState.Selection.Selected
    )

    val state = remember {
        mutableStateOf(data)
    }

    CheckboxRoundMolecule(modifier = Modifier, data = state.value) {
        state.value = state.value.onRadioButtonClick()
    }
}

@Composable
@Preview
fun CheckboxRoundMoleculePreview_disabled() {
    val data = CheckboxRoundMoleculeData(
        id = "",
        title = UiText.DynamicString("Title"),
        description = UiText.DynamicString("Description"),
        status = UiText.DynamicString("status"),
        interactionState = UIState.Interaction.Disabled
    )

    val state = remember {
        mutableStateOf(data)
    }

    CheckboxRoundMolecule(modifier = Modifier, data = state.value) {
        state.value = state.value.onRadioButtonClick()
    }
}

@Composable
@Preview
fun CheckboxRoundMoleculePreview_selected_disabled() {
    val data = CheckboxRoundMoleculeData(
        id = "",
        title = UiText.DynamicString("Title"),
        description = UiText.DynamicString("Description"),
        status = UiText.DynamicString("status"),
        selectionState = UIState.Selection.Selected,
        interactionState = UIState.Interaction.Disabled
    )

    val state = remember {
        mutableStateOf(data)
    }

    CheckboxRoundMolecule(modifier = Modifier, data = state.value) {
        state.value = state.value.onRadioButtonClick()
    }
}
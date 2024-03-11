package ua.gov.diia.ui_base.components.molecule.checkbox

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import ua.gov.diia.ui_base.components.conditional
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.state.UIState
import ua.gov.diia.ui_base.components.noRippleClickable
import ua.gov.diia.ui_base.components.theme.Black
import ua.gov.diia.ui_base.components.theme.BlackAlpha30
import ua.gov.diia.ui_base.components.theme.DiiaTextStyle
import ua.gov.diia.ui_base.components.theme.Mantis
import ua.gov.diia.ui_base.components.theme.White

@Composable
fun CheckboxRoundMlc(
    modifier: Modifier = Modifier,
    data: CheckboxRoundMlcData,
    onUIAction: (UIAction) -> Unit
) {

    Row(
        modifier = modifier
        .padding(all = 16.dp)
        .conditional(data.interactionState == UIState.Interaction.Enabled) {
        noRippleClickable {
            onUIAction(
                UIAction(
                    actionKey = data.actionKey,
                    data = data.id
                )
            )
        }
    },
        verticalAlignment = Alignment.Top
    ) {
        Box(modifier = Modifier
            .background(
                color = when (data.interactionState) {

                    UIState.Interaction.Disabled -> {
                        when (data.selectionState) {
                            UIState.Selection.Selected -> BlackAlpha30
                            UIState.Selection.Unselected -> White
                        }
                    }

                    UIState.Interaction.Enabled -> {
                        when (data.selectionState) {
                            UIState.Selection.Selected -> Mantis
                            UIState.Selection.Unselected -> White
                        }
                    }
                }, shape = CircleShape
            )
            .conditional(data.selectionState == UIState.Selection.Unselected) {
                border(
                    color = when (data.interactionState) {
                        UIState.Interaction.Disabled -> BlackAlpha30
                        UIState.Interaction.Enabled -> Black
                    }, width = 2.dp, shape = CircleShape
                )
            }
            .size(20.dp), contentAlignment = Alignment.Center) {
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
                .align(Alignment.CenterVertically)
        ) {
            Text(
                modifier = Modifier,
                text = data.label,
                color = when (data.interactionState) {
                    UIState.Interaction.Disabled -> BlackAlpha30
                    UIState.Interaction.Enabled -> Black
                },
                style = DiiaTextStyle.t3TextBody
            )
            data.description?.let {
                Text(
                    modifier = Modifier.padding(top = 4.dp),
                    text = data.description,
                    color = BlackAlpha30,
                    style = DiiaTextStyle.t3TextBody
                )
            }
        }
    }
}

data class CheckboxRoundMlcData(
    val actionKey: String = UIActionKeysCompose.CHECKBOX_REGULAR,
    val id: String,
    val label: String,
    val description: String? = null,
    val status: String? = null,
    val interactionState: UIState.Interaction,
    val selectionState: UIState.Selection
) : UIElementData, CheckBoxItem {
    public fun onCheckboxClick(): CheckboxRoundMlcData {
        return this.copy(selectionState = this.selectionState.reverse())
    }
}

@Composable
@Preview
fun CheckboxRoundMlcPreviewNoDescription_Selected() {
    val data = CheckboxRoundMlcData(
        id = "",
        label = "label",
        description = null,
        status = "status",
        interactionState = UIState.Interaction.Enabled,
        selectionState = UIState.Selection.Selected
    )
    val state = remember { mutableStateOf(data) }
    CheckboxRoundMlc(data = data) {
        state.value = state.value.onCheckboxClick()
    }
}

@Composable
@Preview
fun CheckboxRoundMlcPreview_Selected() {
    val data = CheckboxRoundMlcData(
        id = "",
        label = "label",
        description = "description",
        status = "status",
        interactionState = UIState.Interaction.Enabled,
        selectionState = UIState.Selection.Selected
    )
    val state = remember { mutableStateOf(data) }
    CheckboxRoundMlc(data = data) {
        state.value = state.value.onCheckboxClick()
    }
}

@Composable
@Preview
fun CheckboxRoundMlcPreview_UnSelected() {
    val data = CheckboxRoundMlcData(
        id = "",
        label = "label",
        description = "description",
        status = "status",
        interactionState = UIState.Interaction.Enabled,
        selectionState = UIState.Selection.Unselected
    )
    val state = remember { mutableStateOf(data) }
    CheckboxRoundMlc(data = data) {}
}

@Composable
@Preview
fun CheckboxRoundMlcPreview_Disabled() {
    val data = CheckboxRoundMlcData(
        id = "",
        label = "label",
        description = "description",
        status = "status",
        interactionState = UIState.Interaction.Disabled,
        selectionState = UIState.Selection.Unselected
    )
    val state = remember { mutableStateOf(data) }
    CheckboxRoundMlc(data = data) {}
}
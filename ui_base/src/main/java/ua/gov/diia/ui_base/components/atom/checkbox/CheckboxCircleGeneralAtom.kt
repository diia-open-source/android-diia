package ua.gov.diia.ui_base.components.atom.checkbox

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.ui_base.R
import ua.gov.diia.ui_base.components.conditional
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.state.UIState
import ua.gov.diia.ui_base.components.theme.Black
import ua.gov.diia.ui_base.components.theme.BlackAlpha30
import ua.gov.diia.ui_base.components.theme.DiiaTextStyle
import ua.gov.diia.ui_base.components.theme.Mantis
import ua.gov.diia.ui_base.components.theme.White


@Composable
fun CheckboxCircleGeneralAtom(
    modifier: Modifier = Modifier,
    data: CheckboxCircleGeneralAtomData,
    onUIAction: (UIAction) -> Unit
) {
    var selectionState by remember { mutableStateOf(data.selectionState) }

    LaunchedEffect(key1 = selectionState) {
        onUIAction(UIAction(actionKey = data.actionKey, states = listOf(selectionState)))
    }
    Row(modifier = modifier.conditional(data.interactionState == UIState.Interaction.Enabled) {
        clickable {
            selectionState = when (selectionState) {
                UIState.Selection.Selected -> UIState.Selection.Unselected
                UIState.Selection.Unselected -> UIState.Selection.Selected
            }
        }
    }, verticalAlignment = Alignment.CenterVertically) {
        Box(modifier = Modifier
            .background(
                color = when (data.interactionState) {

                    UIState.Interaction.Disabled -> {
                        when (selectionState) {
                            UIState.Selection.Selected -> BlackAlpha30
                            UIState.Selection.Unselected -> White
                        }
                    }

                    UIState.Interaction.Enabled -> {
                        when (selectionState) {
                            UIState.Selection.Selected -> Mantis
                            UIState.Selection.Unselected -> White
                        }
                    }
                }, shape = CircleShape
            )
            .conditional(selectionState == UIState.Selection.Unselected) {
                border(
                    color = when (data.interactionState) {
                        UIState.Interaction.Disabled -> BlackAlpha30
                        UIState.Interaction.Enabled -> Black
                    }, width = 2.dp, shape = CircleShape
                )
            }
            .size(20.dp), contentAlignment = Alignment.Center) {
            if (selectionState == UIState.Selection.Selected) {
                Icon(
                    modifier = Modifier.size(10.dp, 8.dp),
                    painter = painterResource(id = R.drawable.diia_check),
                    contentDescription = null,
                    tint = White
                )
            }
        }

        Text(
            modifier = Modifier.padding(start = 16.dp), text = data.title,
            color = when (data.interactionState) {
                UIState.Interaction.Disabled -> BlackAlpha30
                UIState.Interaction.Enabled -> Black
            },
            style = DiiaTextStyle.t3TextBody
        )
    }
}

data class CheckboxCircleGeneralAtomData(
    val actionKey: String = UIActionKeysCompose.CHECKBOX_REGULAR,
    val id: String,
    val title: String,
    val interactionState: UIState.Interaction,
    val selectionState: UIState.Selection
)

@Composable
@Preview
fun CheckboxCircleGeneralAtomPreview_EnabledState_Checked() {
    val data = CheckboxCircleGeneralAtomData(
        title = "Label",
        id = "",
        interactionState = UIState.Interaction.Enabled,
        selectionState = UIState.Selection.Selected
    )
    CheckboxCircleGeneralAtom(data = data) {

    }
}


@Composable
@Preview
fun CheckboxCircleGeneralAtomPreview_EnabledState_Unchecked() {
    val data = CheckboxCircleGeneralAtomData(
        title = "Label",
        id = "",
        interactionState = UIState.Interaction.Enabled,
        selectionState = UIState.Selection.Selected
    )
    CheckboxCircleGeneralAtom(data = data) {

    }
}

@Composable
@Preview
fun CheckboxCircleGeneralAtomPreview_DisabledState_Checked() {
    val data = CheckboxCircleGeneralAtomData(
        title = "Label",
        id = "",
        interactionState = UIState.Interaction.Disabled,
        selectionState = UIState.Selection.Selected
    )
    CheckboxCircleGeneralAtom(data = data) {

    }
}

@Composable
@Preview
fun CheckboxCircleGeneralAtomPreview_DisabledState_Unchecked() {
    val data = CheckboxCircleGeneralAtomData(
        title = "Label",
        id = "",
        interactionState = UIState.Interaction.Disabled,
        selectionState = UIState.Selection.Unselected
    )
    CheckboxCircleGeneralAtom(data = data) {

    }
}


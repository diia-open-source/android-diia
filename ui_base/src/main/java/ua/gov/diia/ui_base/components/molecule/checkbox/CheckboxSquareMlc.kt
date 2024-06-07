package ua.gov.diia.ui_base.components.molecule.checkbox

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.ui_base.R
import ua.gov.diia.ui_base.components.conditional
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.state.UIState
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.noRippleClickable
import ua.gov.diia.ui_base.components.theme.Black
import ua.gov.diia.ui_base.components.theme.BlackAlpha30
import ua.gov.diia.ui_base.components.theme.DiiaTextStyle
import ua.gov.diia.ui_base.components.theme.Transparent
import ua.gov.diia.ui_base.components.theme.White

@Composable
fun CheckboxSquareMlc(
    modifier: Modifier = Modifier,
    data: CheckboxSquareMlcData,
    onUIAction: (UIAction) -> Unit
) {
    Row(
        modifier = modifier.conditional(data.interactionState == UIState.Interaction.Enabled) {
            noRippleClickable {
                onUIAction(
                    UIAction(
                        actionKey = data.actionKey,
                        data = data.id,
                        optionalId = data.options.firstOrNull {
                             val condition = data.selectionState != UIState.Selection.Selected
                             it.isSelected == condition
                        }?.id
                    )
                )
            }.composed {
                val contentDescription = data.contentDescription?.asString()
                if (contentDescription != null) {
                    this.semantics(mergeDescendants = true) {
                        val state =
                            if (data.selectionState == UIState.Selection.Selected) "Active: " else "Inactive: "
                        stateDescription = state + contentDescription
                    }
                }
                this
            }
        }, verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .background(
                    color = when (data.interactionState) {
                        UIState.Interaction.Disabled -> {
                            when (data.selectionState) {
                                UIState.Selection.Selected -> BlackAlpha30
                                UIState.Selection.Unselected -> Transparent
                            }
                        }

                        UIState.Interaction.Enabled -> {
                            when (data.selectionState) {
                                UIState.Selection.Selected -> Black
                                UIState.Selection.Unselected -> Transparent
                            }
                        }
                    }, shape = RoundedCornerShape(4.dp)
                )
                .conditional(data.selectionState == UIState.Selection.Unselected) {
                    border(
                        color = when (data.interactionState) {
                            UIState.Interaction.Disabled -> BlackAlpha30
                            UIState.Interaction.Enabled -> Black
                        }, width = 2.dp, shape = RoundedCornerShape(4.dp)
                    )
                }
                .size(20.dp)
                .testTag(data.componentId?.asString() ?: ""),
            contentAlignment = Alignment.Center
        ) {
            if (data.selectionState == UIState.Selection.Selected) {
                Icon(
                    modifier = Modifier.fillMaxSize().padding(5.dp),
                    painter = painterResource(id = R.drawable.diia_check),
                    contentDescription = null,
                    tint = White
                )
            }
        }

        Text(
            modifier = Modifier.padding(start = 12.dp, top = 2.dp),
            text = data.title.asString(),
            color = when (data.interactionState) {
                UIState.Interaction.Disabled -> BlackAlpha30
                UIState.Interaction.Enabled -> Black
            },
            style = DiiaTextStyle.t3TextBody
        )
    }
}

data class CheckboxSquareMlcData(
    val actionKey: String = UIActionKeysCompose.CHECKBOX_REGULAR,
    val id: String = "",
    val title: UiText,
    val contentDescription: UiText? = null,
    val interactionState: UIState.Interaction,
    val selectionState: UIState.Selection,
    val options: List<Option> = listOf(),
    val componentId: UiText? = null,
) : UIElementData, Cloneable {

    public override fun clone(): CheckboxSquareMlcData {
        return super.clone() as CheckboxSquareMlcData
    }

    public fun onCheckboxClick(): CheckboxSquareMlcData {
        return this.copy(selectionState = this.selectionState.reverse())
    }

    fun getCurrentOption() : Option? {
        return options.firstOrNull {
            val condition = selectionState == UIState.Selection.Selected
            it.isSelected == condition
        }
    }
    data class Option(
        val id: String,
        val isSelected: Boolean
    )
}

fun String?.toUIModel(
    actionKey: String = UIActionKeysCompose.CHECKBOX_REGULAR,
    id: String = "",
    interactionState: UIState.Interaction = UIState.Interaction.Enabled,
    selectionState: UIState.Selection = UIState.Selection.Unselected
): CheckboxSquareMlcData? {
    if (this == null) return null
    return CheckboxSquareMlcData(
        actionKey = actionKey,
        id = id,
        title = UiText.DynamicString(this),
        interactionState = interactionState,
        selectionState = selectionState
    )
}

@Composable
@Preview
fun CheckboxSquareAtomPreview_EnabledState_Checked() {
    val data = CheckboxSquareMlcData(
        title = UiText.DynamicString("Label"),
        id = "",
        interactionState = UIState.Interaction.Enabled,
        selectionState = UIState.Selection.Selected
    )
    val state = remember { mutableStateOf(data) }

    CheckboxSquareMlc(data = state.value) {
        state.value = state.value.onCheckboxClick()
    }
}

@Composable
@Preview
fun CheckboxSquareAtomPreview_EnabledState_Unchecked() {
    val data = CheckboxSquareMlcData(
        title = UiText.DynamicString("Label"),
        id = "",
        interactionState = UIState.Interaction.Enabled,
        selectionState = UIState.Selection.Unselected
    )
    val state = remember { mutableStateOf(data) }
    CheckboxSquareMlc(data = state.value) {
        state.value = state.value.onCheckboxClick()
    }
}

@Composable
@Preview
fun CheckboxSquareAtomPreview_DisabledState_Checked() {
    val data = CheckboxSquareMlcData(
        title = UiText.DynamicString("Label"),
        id = "",
        interactionState = UIState.Interaction.Disabled,
        selectionState = UIState.Selection.Selected
    )
    val state = remember { mutableStateOf(data) }
    CheckboxSquareMlc(data = state.value) {
        state.value = state.value.onCheckboxClick()
    }
}

@Composable
@Preview
fun CheckboxSquareAtomPreview_DisabledState_Unchecked() {
    val data = CheckboxSquareMlcData(
        title = UiText.DynamicString("Label"),
        id = "",
        interactionState = UIState.Interaction.Disabled,
        selectionState = UIState.Selection.Unselected
    )
    val state = remember { mutableStateOf(data) }
    CheckboxSquareMlc(data = state.value) {
        state.value = state.value.onCheckboxClick()
    }
}

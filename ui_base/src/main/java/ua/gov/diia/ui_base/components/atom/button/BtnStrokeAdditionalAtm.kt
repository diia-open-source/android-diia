package ua.gov.diia.ui_base.components.atom.button

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.core.models.common_compose.atm.button.BtnStrokeAdditionalAtm
import ua.gov.diia.core.models.common_compose.general.ButtonStates
import ua.gov.diia.ui_base.components.infrastructure.DataActionWrapper
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.state.UIState
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.toDynamicString
import ua.gov.diia.ui_base.components.subatomic.loader.LoaderCircularEclipse23Subatomic
import ua.gov.diia.ui_base.components.theme.Black
import ua.gov.diia.ui_base.components.theme.BlackAlpha10
import ua.gov.diia.ui_base.components.theme.BlackAlpha30
import ua.gov.diia.ui_base.components.theme.DiiaTextStyle
import ua.gov.diia.ui_base.components.theme.Transparent
import ua.gov.diia.ui_base.util.toDataActionWrapper


@Composable
fun BtnStrokeAdditionalAtm(
    modifier: Modifier = Modifier,
    data: ButtonStrokeAdditionalAtomData,
    progressIndicator: Pair<String, Boolean> = Pair("", false),
    onUIAction: (UIAction) -> Unit
) {
    Button(
        modifier = modifier.testTag(data.componentId?.asString() ?: ""),
        colors = ButtonDefaults.buttonColors(
            containerColor = Transparent,
            disabledContainerColor = Transparent
        ),
        border = BorderStroke(
            width = 2.dp, color = when (data.interactionState) {
                UIState.Interaction.Disabled -> BlackAlpha10
                UIState.Interaction.Enabled -> Black
            }
        ),
        enabled = data.interactionState == UIState.Interaction.Enabled,
        onClick = {
            onUIAction(
                UIAction(
                    actionKey = data.actionKey,
                    data = data.id,
                    action = data.action
                )
            )
        }
    ) {
        AnimatedVisibility(visible = progressIndicator.first == data.id && progressIndicator.second == true) {
            Row {
                LoaderCircularEclipse23Subatomic(modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
            }
        }
        Text(
            text = data.title.asString(),
            color = when (data.interactionState) {
                UIState.Interaction.Disabled -> BlackAlpha30
                UIState.Interaction.Enabled -> Black
            },
            style = DiiaTextStyle.t2TextDescription,

            )
    }
}

data class ButtonStrokeAdditionalAtomData(
    val actionKey: String = UIActionKeysCompose.BUTTON_REGULAR,
    val componentId: UiText? = null,
    val id: String = "",
    val title: UiText,
    val interactionState: UIState.Interaction = UIState.Interaction.Enabled,
    val action: DataActionWrapper? = null
)

fun BtnStrokeAdditionalAtm.toUIModel(
    id: String = ""
): ButtonStrokeAdditionalAtomData {
    return ButtonStrokeAdditionalAtomData(
        title = label.toDynamicString(),
        componentId = componentId?.let { UiText.DynamicString(it) },
        id = id,
        interactionState = state?.let {
            when (state) {
                ButtonStates.enabled -> UIState.Interaction.Enabled
                ButtonStates.disabled -> UIState.Interaction.Disabled
                ButtonStates.invisible -> UIState.Interaction.Disabled
                else -> UIState.Interaction.Enabled
            }
        } ?: UIState.Interaction.Enabled,
        action = action?.toDataActionWrapper()
    )
}

@Composable
@Preview
fun ButtonStrokeAdditionalAtomPreview_EnabledState() {
    val buttonStateEnabled = ButtonStrokeAdditionalAtomData(
        title = UiText.DynamicString("Label"),
        id = "",
        interactionState = UIState.Interaction.Enabled
    )
    BtnStrokeAdditionalAtm(data = buttonStateEnabled) {
    }
}

@Composable
@Preview
fun ButtonStrokeAdditionalAtomPreview_DisabledState() {
    val buttonStateDisabled = ButtonStrokeAdditionalAtomData(
        title = UiText.DynamicString("Label"),
        id = "",
        interactionState = UIState.Interaction.Disabled
    )
    BtnStrokeAdditionalAtm(data = buttonStateDisabled) {
    }
}

@Composable
@Preview
fun ButtonStrokeAdditionalAtomPreview_LoadingState() {
    val buttonStateLoading = ButtonStrokeAdditionalAtomData(
        title = UiText.DynamicString("Label"),
        id = "id",
        interactionState = UIState.Interaction.Enabled
    )
    BtnStrokeAdditionalAtm(
        data = buttonStateLoading, progressIndicator = Pair("id", true)
    ) {
    }
}

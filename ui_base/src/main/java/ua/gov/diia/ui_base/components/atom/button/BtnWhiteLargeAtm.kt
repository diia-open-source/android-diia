package ua.gov.diia.ui_base.components.atom.button

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
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
import ua.gov.diia.core.models.common_compose.atm.button.BtnWhiteLargeAtm
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
import ua.gov.diia.ui_base.components.theme.DiiaTextStyle
import ua.gov.diia.ui_base.components.theme.White
import ua.gov.diia.ui_base.components.theme.WhiteAlpha50
import ua.gov.diia.ui_base.util.toDataActionWrapper


@Composable
fun BtnWhiteLargeAtm(
    modifier: Modifier = Modifier,
    data: BtnWhiteLargeAtmData,
    progressIndicator: Pair<String, Boolean> = Pair("", false),
    onUIAction: (UIAction) -> Unit
) {
    Button(
        modifier = modifier
            .defaultMinSize(
                minWidth = 311.dp
            )
            .testTag(data.componentId?.asString() ?: ""),
        colors = ButtonDefaults.buttonColors(
            containerColor = White,
            disabledContainerColor = WhiteAlpha50
        ),
        enabled = data.interactionState == UIState.Interaction.Enabled,
        onClick = {
            onUIAction(UIAction(actionKey = data.actionKey, data = data.id, action = data.action))
        }
    ) {
        AnimatedVisibility(visible = data.id == progressIndicator.first && progressIndicator.second) {
            Row {
                LoaderCircularEclipse23Subatomic(modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
            }
        }
        Text(
            modifier = Modifier.padding(vertical = 8.dp),
            text = data.title.asString(),
            color = when (data.interactionState) {
                UIState.Interaction.Disabled -> BlackAlpha10
                UIState.Interaction.Enabled -> Black
            },
            style = DiiaTextStyle.h4ExtraSmallHeading
        )
    }
}

data class BtnWhiteLargeAtmData(
    val actionKey: String = UIActionKeysCompose.BUTTON_REGULAR,
    val id: String,
    val title: UiText,
    val interactionState: UIState.Interaction,
    val action: DataActionWrapper? = null,
    val componentId: UiText? = null
)

fun BtnWhiteLargeAtm.toUIModel(
    id: String = "",
): BtnWhiteLargeAtmData {
    return BtnWhiteLargeAtmData(
        id = id,
        title = label.toDynamicString(),
        action = action?.toDataActionWrapper(),
        interactionState = state?.let {
            when (state) {
                ButtonStates.enabled -> UIState.Interaction.Enabled
                ButtonStates.disabled -> UIState.Interaction.Disabled
                ButtonStates.invisible -> UIState.Interaction.Disabled
                else -> UIState.Interaction.Enabled
            }
        } ?: UIState.Interaction.Enabled,
    )
}


@Composable
@Preview
fun ButtonWhiteLargeAtomPreview_EnabledState() {
    val buttonStateEnabled = BtnWhiteLargeAtmData(
        title = "Label".toDynamicString(),
        id = "",
        interactionState = UIState.Interaction.Enabled
    )
    BtnWhiteLargeAtm(data = buttonStateEnabled) {
    }
}

@Composable
@Preview
fun ButtonWhiteLargeAtomPreview_DisabledState() {
    val buttonStateDisabled = BtnWhiteLargeAtmData(
        title = "Label".toDynamicString(),
        id = "",
        interactionState = UIState.Interaction.Disabled
    )
    BtnWhiteLargeAtm(data = buttonStateDisabled) {
    }
}

@Composable
@Preview
fun ButtonWhiteLargeAtomPreview_LoadingState() {
    val buttonStateLoading = BtnWhiteLargeAtmData(
        title = "Label".toDynamicString(),
        id = "id",
        interactionState = UIState.Interaction.Enabled
    )
    BtnWhiteLargeAtm(
        data = buttonStateLoading,
        progressIndicator = Pair("id", true)
    ) {
    }
}

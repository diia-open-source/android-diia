package ua.gov.diia.ui_base.components.atom.button

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.state.UIState
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.subatomic.loader.LoaderCircularEclipse23Subatomic
import ua.gov.diia.ui_base.components.theme.Black
import ua.gov.diia.ui_base.components.theme.BlackAlpha10
import ua.gov.diia.ui_base.components.theme.DiiaTextStyle
import ua.gov.diia.ui_base.components.theme.White

@Composable
fun BtnPrimaryAdditionalAtm(
    modifier: Modifier = Modifier,
    data: BtnPrimaryAdditionalAtmData,
    progressIndicator: Pair<String, Boolean> = Pair("", false),
    onUIAction: (UIAction) -> Unit
) {
    Button(
        modifier = modifier.padding(top = 16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Black,
            disabledContainerColor = BlackAlpha10
        ),
        enabled = data.interactionState == UIState.Interaction.Enabled,
        onClick = {
            onUIAction(UIAction(actionKey = data.actionKey, data = data.id))
        }
    ) {
        AnimatedVisibility(visible = data.id == progressIndicator.first && progressIndicator.second == true) {
            Row {
                LoaderCircularEclipse23Subatomic(modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
            }
        }
        Text(
            text = data.title.asString(),
            color = White,
            style = DiiaTextStyle.t2TextDescription
        )
    }
}

data class BtnPrimaryAdditionalAtmData(
    val actionKey: String = UIActionKeysCompose.BUTTON_REGULAR,
    val id: String = "",
    val title: UiText,
    val interactionState: UIState.Interaction = UIState.Interaction.Enabled
)

@Composable
@Preview
fun BtnPrimaryAdditionalAtmPreview_EnabledState() {
    val buttonStateEnabled = BtnPrimaryAdditionalAtmData(
        title = UiText.DynamicString("Label"),
        id = "",
        interactionState = UIState.Interaction.Enabled
    )
    BtnPrimaryAdditionalAtm(data = buttonStateEnabled) {
    }
}

@Composable
@Preview
fun BtnPrimaryAdditionalAtmPreview_DisabledState() {
    val buttonStateDisabled = BtnPrimaryAdditionalAtmData(
        title = UiText.DynamicString("Label"),
        id = "",
        interactionState = UIState.Interaction.Disabled
    )
    BtnPrimaryAdditionalAtm(data = buttonStateDisabled) {
    }
}

@Composable
@Preview
fun BtnPrimaryAdditionalAtmPreview_LoadingState() {
    val buttonStateLoading = BtnPrimaryAdditionalAtmData(
        title = UiText.DynamicString("Label"),
        id = "id",
        interactionState = UIState.Interaction.Enabled
    )
    BtnPrimaryAdditionalAtm(
        data = buttonStateLoading,
        progressIndicator = Pair("id", true)
    ) {
    }
}


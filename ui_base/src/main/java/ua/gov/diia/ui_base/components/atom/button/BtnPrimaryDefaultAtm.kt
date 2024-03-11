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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
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
fun BtnPrimaryDefaultAtm(
    modifier: Modifier = Modifier,
    data: BtnPrimaryDefaultAtmData,
    progressIndicator: Pair<String, Boolean> = Pair("", false),
    onUIAction: (UIAction) -> Unit
) {
    val isLoading = remember {
        mutableStateOf(data.id == progressIndicator.first && progressIndicator.second)
    }

    LaunchedEffect(key1 = data.id == progressIndicator.first, key2 = progressIndicator.second) {
        isLoading.value = data.id == progressIndicator.first && progressIndicator.second
    }
    Button(
        modifier = modifier
            .padding(top = 16.dp)
            .defaultMinSize(minWidth = 160.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Black,
            disabledContainerColor = BlackAlpha10
        ),
        enabled = data.interactionState == UIState.Interaction.Enabled,
        onClick = {
            if (!isLoading.value) {
                onUIAction(UIAction(actionKey = data.actionKey, data = data.id))
            }
        }
    ) {
        AnimatedVisibility(visible = isLoading.value) {
            Row {
                LoaderCircularEclipse23Subatomic(modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
            }
        }
        Text(
            modifier = Modifier.padding(vertical = 8.dp),
            text = data.title.asString(),
            color = White,
            style = DiiaTextStyle.t1BigText
        )
    }
}

data class BtnPrimaryDefaultAtmData(
    val actionKey: String = UIActionKeysCompose.BUTTON_REGULAR,
    val id: String = "",
    val title: UiText,
    val interactionState: UIState.Interaction = UIState.Interaction.Enabled
) : UIElementData

@Composable
@Preview
fun BtnPrimaryDefaultAtmPreview_EnabledState() {
    val buttonStateEnabled =
        BtnPrimaryDefaultAtmData(
            title = UiText.DynamicString("Label"),
            id = "",
            interactionState = UIState.Interaction.Enabled
        )
    BtnPrimaryDefaultAtm(data = buttonStateEnabled) {
    }
}

@Composable
@Preview
fun BtnPrimaryDefaultAtmPreview_DisabledState() {
    val buttonStateDisabled =
        BtnPrimaryDefaultAtmData(
            title = UiText.DynamicString("Label"),
            id = "",
            interactionState = UIState.Interaction.Disabled
        )
    BtnPrimaryDefaultAtm(data = buttonStateDisabled) {
    }
}

@Composable
@Preview
fun BtnPrimaryDefaultAtmPreview_LoadingState() {
    val buttonStateLoading =
        BtnPrimaryDefaultAtmData(
            title = UiText.DynamicString("Label"),
            id = "id",
            interactionState = UIState.Interaction.Enabled
        )
    BtnPrimaryDefaultAtm(
        data = buttonStateLoading,
        progressIndicator = Pair("id", true)
    ) {
    }
}


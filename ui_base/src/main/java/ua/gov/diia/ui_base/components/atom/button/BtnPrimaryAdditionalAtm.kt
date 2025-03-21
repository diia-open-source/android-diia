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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.core.models.common_compose.atm.button.BtnPrimaryAdditionalAtm
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
import ua.gov.diia.ui_base.util.toDataActionWrapper

@Composable
fun BtnPrimaryAdditionalAtm(
    modifier: Modifier = Modifier,
    data: BtnPrimaryAdditionalAtmData,
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
            .defaultMinSize(minWidth = 96.dp)
            .testTag(data.componentId?.asString() ?: ""),
        colors = ButtonDefaults.buttonColors(
            containerColor = Black,
            disabledContainerColor = BlackAlpha10
        ),
        enabled = data.interactionState == UIState.Interaction.Enabled,
        onClick = {
            if (!isLoading.value) {
                onUIAction(
                    UIAction(
                        actionKey = data.actionKey,
                        data = data.id,
                        action = data.action
                    )
                )
            }
        }
    ) {
        AnimatedVisibility(visible = isLoading.value) {
            LoaderCircularEclipse23Subatomic(modifier = Modifier.size(18.dp))

        }

        if(!isLoading.value){
            Text(
                text = data.title.asString(),
                color = White,
                style = DiiaTextStyle.t2TextDescription
            )
        }
    }
}

data class BtnPrimaryAdditionalAtmData(
    val actionKey: String = UIActionKeysCompose.BUTTON_REGULAR,
    val componentId: UiText? = null,
    val id: String = "",
    val title: UiText,
    val action: DataActionWrapper? = null,
    val interactionState: UIState.Interaction = UIState.Interaction.Enabled
)

fun BtnPrimaryAdditionalAtm.toUIModel(
    id: String = UIActionKeysCompose.BUTTON_REGULAR,
): BtnPrimaryAdditionalAtmData {
    return BtnPrimaryAdditionalAtmData(
        componentId = componentId?.let { UiText.DynamicString(it) },
        id = id,
        title = label.toDynamicString(),
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


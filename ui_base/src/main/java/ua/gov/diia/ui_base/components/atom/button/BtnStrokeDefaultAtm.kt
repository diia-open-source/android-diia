package ua.gov.diia.ui_base.components.atom.button

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.core.models.common_compose.atm.button.BtnStrokeDefaultAtm
import ua.gov.diia.core.models.common_compose.general.ButtonStates
import ua.gov.diia.ui_base.components.infrastructure.DataActionWrapper
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.state.UIState
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.toDynamicString
import ua.gov.diia.ui_base.components.subatomic.loader.LoaderCircularEclipse23Subatomic
import ua.gov.diia.ui_base.components.theme.Black
import ua.gov.diia.ui_base.components.theme.BlackAlpha10
import ua.gov.diia.ui_base.components.theme.DiiaTextStyle
import ua.gov.diia.ui_base.util.toDataActionWrapper

@Composable
fun BtnStrokeDefaultAtm(
    modifier: Modifier = Modifier,
    progressIndicator: Pair<String, Boolean> = Pair("", false),
    data: BtnStrokeDefaultAtmData,
    onUIAction: (UIAction) -> Unit
) {

    val isLoading = remember {
        mutableStateOf(data.id == progressIndicator.first && progressIndicator.second)
    }

    LaunchedEffect(key1 = data.id == progressIndicator.first, key2 = progressIndicator.second) {
        isLoading.value = data.id == progressIndicator.first && progressIndicator.second
    }

    Button(modifier = modifier
        .padding(top = 16.dp)
        .padding(horizontal = 40.dp, vertical = 16.dp)
        .defaultMinSize(minWidth = 160.dp)
        .testTag(data.componentId?.asString() ?: ""),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent, disabledContainerColor = Color.Transparent
        ), border = BorderStroke(
            width = 2.dp, color = when (data.interactionState) {
                UIState.Interaction.Disabled -> BlackAlpha10
                UIState.Interaction.Enabled -> Black
            }
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
            LoaderCircularEclipse23Subatomic(
                modifier = Modifier.padding(vertical = 7.dp).size(18.dp))

        }
        if(!isLoading.value){
            Text(
                modifier = Modifier.padding(vertical = 8.dp),
                text = data.title.asString(),
                color = if (data.interactionState == UIState.Interaction.Disabled) {
                    BlackAlpha10
                } else {
                    Black
                }, style = DiiaTextStyle.t1BigText
            )
        }
    }
}

data class BtnStrokeDefaultAtmData(
    val actionKey: String = UIActionKeysCompose.BUTTON_REGULAR,
    val componentId: UiText? = null,
    val id: String,
    val title: UiText,
    val interactionState: UIState.Interaction,
    val action: DataActionWrapper? = null,
) : UIElementData

fun BtnStrokeDefaultAtm.toUIModel(
    id: String = UIActionKeysCompose.BUTTON_REGULAR,
    componentIdExternal: UiText? = null
): BtnStrokeDefaultAtmData {
    return BtnStrokeDefaultAtmData(
        componentId = componentIdExternal ?: componentId.orEmpty().toDynamicString(),
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
fun BtnStrokeDefaultAtmPreview_EnabledState() {
    val buttonStateEnabled = BtnStrokeDefaultAtmData(
        title = UiText.DynamicString("Label"),
        id = "",
        interactionState = UIState.Interaction.Enabled
    )
    BtnStrokeDefaultAtm(data = buttonStateEnabled) {
    }
}

@Composable
@Preview
fun BtnStrokeDefaultAtmPreview_DisabledState() {
    val buttonStateDisabled = BtnStrokeDefaultAtmData(
        title = UiText.DynamicString("Label"),
        id = "",
        interactionState = UIState.Interaction.Disabled
    )
    BtnStrokeDefaultAtm(data = buttonStateDisabled) {
    }
}

@Composable
@Preview
fun BtnStrokeDefaultAtmPreview_LoadingState() {
    val buttonStateLoading = BtnStrokeDefaultAtmData(
        title = UiText.DynamicString("Label"),
        id = "id",
        interactionState = UIState.Interaction.Enabled
    )
    BtnStrokeDefaultAtm(
        data = buttonStateLoading,
        progressIndicator = Pair("id", true)
    ) {
    }
}

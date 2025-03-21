package ua.gov.diia.ui_base.components.atom.button

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.core.models.common_compose.atm.button.BtnPrimaryDefaultAtm
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
import ua.gov.diia.ui_base.components.theme.White
import ua.gov.diia.ui_base.util.toDataActionWrapper
import ua.gov.diia.ui_base.util.toDataActionsWrapper

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
            .defaultMinSize(minWidth = 160.dp)
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
                        action = data.action,
                        actions = data.actions
                    )
                )
            }
        }
    ) {
        Box {
            this@Button.AnimatedVisibility(
                modifier = Modifier.align(Alignment.Center),
                visible = isLoading.value,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                LoaderCircularEclipse23Subatomic(
                    modifier = Modifier
                        .padding(vertical = 7.dp)
                        .size(18.dp)
                )
            }
            Text(
                modifier = Modifier
                    .padding(top = 8.dp, bottom = 8.dp)
                    .alpha(if (isLoading.value) 0.0f else 1f),
                text = data.title.asString(),
                color = White,
                style = DiiaTextStyle.t1BigText
            )
        }

    }
}

data class BtnPrimaryDefaultAtmData(
    val actionKey: String = UIActionKeysCompose.BUTTON_REGULAR,
    val componentId: UiText? = null,
    val id: String = "",
    val title: UiText,
    val interactionState: UIState.Interaction = UIState.Interaction.Enabled,
    val action: DataActionWrapper? = null,
    val actions: List<DataActionWrapper>? = null
) : UIElementData {

    fun changeStateByValidation(state: UIState.Interaction): BtnPrimaryDefaultAtmData {
        return this.copy(
            interactionState = state
        )
    }
}

fun BtnPrimaryDefaultAtm.toUIModel(
    id: String = if (componentId.isNullOrBlank()) UIActionKeysCompose.BUTTON_REGULAR else componentId.orEmpty(),
    componentIdExternal: UiText? = null
): BtnPrimaryDefaultAtmData {
    return BtnPrimaryDefaultAtmData(
        componentId = componentIdExternal ?: componentId.orEmpty().toDynamicString(),
        title = label.toDynamicString(),
        id = id,
        interactionState = state?.let {
            when (state) {
                ButtonStates.enabled -> UIState.Interaction.Enabled
                ButtonStates.disabled -> UIState.Interaction.Disabled
                ButtonStates.invisible -> UIState.Interaction.Disabled
                else -> UIState.Interaction.Enabled
            }
        } ?: UIState.Interaction.Enabled,
        action = action?.toDataActionWrapper(),
        actions = actions?.toDataActionsWrapper()
    )
}

fun String?.toComposeBtnPrimaryDefaultAtm(
    actionKey: String = UIActionKeysCompose.BUTTON_REGULAR,
    id: String = "",
    interactionState: UIState.Interaction = UIState.Interaction.Enabled
): BtnPrimaryDefaultAtmData? {
    if (this == null) return null
    return BtnPrimaryDefaultAtmData(
        actionKey = actionKey,
        id = id,
        title = UiText.DynamicString(this),
        interactionState = interactionState
    )
}

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
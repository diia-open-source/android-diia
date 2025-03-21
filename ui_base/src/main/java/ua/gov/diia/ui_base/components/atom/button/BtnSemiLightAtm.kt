package ua.gov.diia.ui_base.components.atom.button

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.core.models.common_compose.atm.button.BtnSemiLightAtm
import ua.gov.diia.core.models.common_compose.general.ButtonStates
import ua.gov.diia.ui_base.components.infrastructure.DataActionWrapper
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.state.UIState
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.toDynamicString
import ua.gov.diia.ui_base.components.noRippleClickable
import ua.gov.diia.ui_base.components.theme.Black
import ua.gov.diia.ui_base.components.theme.DiiaTextStyle
import ua.gov.diia.ui_base.components.theme.WhiteAlpha40
import ua.gov.diia.ui_base.util.toDataActionWrapper

@Composable
fun BtnSemiLightAtm(
    modifier: Modifier = Modifier,
    data: BtnSemiLightAtmData,
    onUIAction: (UIAction) -> Unit
) {
    Box(
        modifier = modifier
            .noRippleClickable {
                onUIAction(
                    UIAction(
                        actionKey = data.actionKey,
                        data = data.id,
                        action = data.action
                    )
                )
            }
            .background(
                color = WhiteAlpha40, shape = RoundedCornerShape(20.dp)
            )
            .padding(horizontal = 12.dp)
            .defaultMinSize(minHeight = 32.dp)
            .testTag(data.componentId?.asString() ?: ""),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = data.label.asString(),
            color = Black,
            style = DiiaTextStyle.t4TextSmallDescription
        )
    }
}

data class BtnSemiLightAtmData(
    val actionKey: String = UIActionKeysCompose.BTN_SEMI_LIGHT_ATM,
    val id: String? = null,
    val componentId: UiText? = null,
    val label: UiText,
    val interactionState: UIState.Interaction = UIState.Interaction.Enabled,
    val action: DataActionWrapper? = null
) : UIElementData

fun BtnSemiLightAtm.toUIModel(): BtnSemiLightAtmData {
    return BtnSemiLightAtmData(
        label = label.toDynamicString(),
        componentId = componentId?.let { UiText.DynamicString(it) },
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
fun BtnSemiLightAtmPreview() {
    val data = BtnSemiLightAtmData(
        label = UiText.DynamicString("Label"),
        id = "",
        interactionState = UIState.Interaction.Enabled
    )
    BtnSemiLightAtm(data = data) {
    }
}
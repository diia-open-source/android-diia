package ua.gov.diia.ui_base.components.atom.button

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.core.models.common_compose.atm.button.BtnWhiteAdditionalIconAtm
import ua.gov.diia.core.models.common_compose.general.ButtonStates
import ua.gov.diia.ui_base.components.DiiaResourceIcon
import ua.gov.diia.ui_base.components.atom.icon.BadgeCounterAtm
import ua.gov.diia.ui_base.components.atom.icon.BadgeCounterAtmData
import ua.gov.diia.ui_base.components.infrastructure.DataActionWrapper
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.state.UIState
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiIcon
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.toDynamicString
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.toDynamicStringOrNull
import ua.gov.diia.ui_base.components.molecule.input.InputPhoneMlcData
import ua.gov.diia.ui_base.components.noRippleClickable
import ua.gov.diia.ui_base.components.subatomic.icon.UiIconWrapperSubatomic
import ua.gov.diia.ui_base.components.theme.Black
import ua.gov.diia.ui_base.components.theme.BlackAlpha30
import ua.gov.diia.ui_base.components.theme.DiiaTextStyle
import ua.gov.diia.ui_base.components.theme.White
import ua.gov.diia.ui_base.util.toDataActionWrapper
import ua.gov.diia.ui_base.util.toUiModel

@Composable
fun BtnWhiteAdditionalIconAtm(
    modifier: Modifier = Modifier,
    data: BtnWhiteAdditionalIconAtmData,
    onUIAction: (UIAction) -> Unit
) {
    Row(
        modifier = modifier
            .background(
                color = White,
                shape = RoundedCornerShape(20.dp)
            )
            .padding(horizontal = 18.dp, vertical = 8.dp)
            .noRippleClickable {
                if (data.interactionState == UIState.Interaction.Enabled) {
                    onUIAction(
                        UIAction(
                            actionKey = data.actionKey,
                            data = data.componentId.toString(),
                            action = data.action
                        )
                    )
                }
            }
            .testTag(data.componentId?.asString() ?: ""),
        verticalAlignment = Alignment.CenterVertically
    ) {
        UiIconWrapperSubatomic(
            modifier = Modifier
                .size(24.dp)
                .alpha(
                    if (data.interactionState == UIState.Interaction.Enabled) 1.0f else 0.3f
                ),
            icon = UiIcon.DrawableResource(DiiaResourceIcon.FILTER.code)
        )
        data.label?.let {
            Text(
                modifier = Modifier.padding(start = 8.dp),
                text = it.asString(),
                color = when (data.interactionState) {
                    UIState.Interaction.Disabled -> BlackAlpha30
                    UIState.Interaction.Enabled -> Black
                },
                style = DiiaTextStyle.t3TextBody
            )
        }
        data.badge?.let { b ->
            if (b.count != 0) {
                BadgeCounterAtm(
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .height(20.dp)
                        .defaultMinSize(minWidth = 20.dp)
                        .alpha(
                            if (data.interactionState == UIState.Interaction.Enabled) 1.0f else 0.3f
                        ),
                    data = data.badge
                )
            }
        }
    }
}

data class BtnWhiteAdditionalIconAtmData(
    val componentId: UiText? = null,
    val actionKey: String = UIActionKeysCompose.BUTTON_WHITE_ADDITIONAL,
    val label: UiText? = null,
    val icon: UiIcon,
    val interactionState: UIState.Interaction,
    val badge: BadgeCounterAtmData? = null,
    val action: DataActionWrapper? = null,
) {
    fun onBadgeChanged(newValue: Int): BtnWhiteAdditionalIconAtmData {
        return this.copy(
            badge = BadgeCounterAtmData(newValue)
        )
    }
}

fun BtnWhiteAdditionalIconAtm.toUIModel(id: String = ""): BtnWhiteAdditionalIconAtmData {
    return BtnWhiteAdditionalIconAtmData(
        componentId = UiText.DynamicString(this.componentId.orEmpty()),
        label = label.toDynamicStringOrNull(),
        icon = UiIcon.DrawableResource(icon),
        interactionState = state?.let {
            when (state) {
                ButtonStates.enabled -> UIState.Interaction.Enabled
                ButtonStates.disabled -> UIState.Interaction.Disabled
                ButtonStates.invisible -> UIState.Interaction.Disabled
                else -> UIState.Interaction.Enabled
            }
        } ?: UIState.Interaction.Enabled,
        badge = badgeCounterAtm?.toUiModel(),
        action = action?.toDataActionWrapper(),
    )
}

@Composable
@Preview
fun BtnWhiteAdditionalIconAtmPreview_Icon() {
    val buttonStateLoading = BtnWhiteAdditionalIconAtmData(
        icon = UiIcon.DrawableResource(DiiaResourceIcon.FILTER.code),
        interactionState = UIState.Interaction.Enabled
    )
    BtnWhiteAdditionalIconAtm(
        data = buttonStateLoading,
    ) {
    }
}

@Composable
@Preview
fun BtnWhiteAdditionalIconAtmPreview_Icon_Label() {
    val buttonStateLoading = BtnWhiteAdditionalIconAtmData(
        label = "Label".toDynamicString(),
        icon = UiIcon.DrawableResource(DiiaResourceIcon.FILTER.code),
        interactionState = UIState.Interaction.Enabled
    )
    BtnWhiteAdditionalIconAtm(
        data = buttonStateLoading,
    ) {
    }
}

@Composable
@Preview
fun BtnWhiteAdditionalIconAtmPreview_Icon_Label_Badge() {
    val buttonStateEnabled = BtnWhiteAdditionalIconAtmData(
        label = "Label".toDynamicString(),
        icon = UiIcon.DrawableResource(DiiaResourceIcon.FILTER.code),
        interactionState = UIState.Interaction.Enabled,
        badge = BadgeCounterAtmData(1)
    )
    BtnWhiteAdditionalIconAtm(data = buttonStateEnabled) {
    }
}

@Composable
@Preview
fun BtnWhiteAdditionalIconAtmPreview_DisabledState() {
    val buttonStateDisabled = BtnWhiteAdditionalIconAtmData(
        label = "Label".toDynamicString(),
        icon = UiIcon.DrawableResource(DiiaResourceIcon.FILTER.code),
        interactionState = UIState.Interaction.Disabled,
        badge = BadgeCounterAtmData(1)

    )
    BtnWhiteAdditionalIconAtm(data = buttonStateDisabled) {
    }
}

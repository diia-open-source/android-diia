package ua.gov.diia.ui_base.components.atom.button

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import ua.gov.diia.core.models.common_compose.atm.button.BtnLoadPlainIconAtm
import ua.gov.diia.core.models.common_compose.general.ButtonStates
import ua.gov.diia.ui_base.components.DiiaResourceIcon
import ua.gov.diia.ui_base.components.conditional
import ua.gov.diia.ui_base.components.infrastructure.DataActionWrapper
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.state.UIState
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiIcon
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.toDynamicString
import ua.gov.diia.ui_base.components.noRippleClickable
import ua.gov.diia.ui_base.components.subatomic.icon.UiIconWrapperSubatomic
import ua.gov.diia.ui_base.components.subatomic.loader.LoaderCircularEclipse23Subatomic
import ua.gov.diia.ui_base.components.theme.Black
import ua.gov.diia.ui_base.components.theme.DiiaTextStyle
import ua.gov.diia.ui_base.util.toDataActionWrapper


@Composable
fun BtnLoadPlainIconAtm(
    modifier: Modifier = Modifier,
    data: BtnLoadPlainIconAtmData,
    progressIndicator: Pair<String, Boolean> = Pair("", false),
    onUIAction: (UIAction) -> Unit
) {
    Row(
        modifier = modifier
            .noRippleClickable {
                if (data.interactionState == UIState.Interaction.Disabled ||
                    (progressIndicator.first.isNotEmpty() && progressIndicator.first == data.id && progressIndicator.second)
                ) return@noRippleClickable
                onUIAction(
                    UIAction(
                        actionKey = data.actionKey,
                        data = data.id,
                        action = data.action
                    )
                )
            }
            .padding(16.dp)
            .fillMaxWidth()
            .conditional(data.interactionState == UIState.Interaction.Disabled) {
                alpha(0.3f)
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        AnimatedVisibility(visible = progressIndicator.first == data.id && data.id.isNotEmpty() && progressIndicator.second) {
            Box(modifier = Modifier.size(24.dp)) {
                LoaderCircularEclipse23Subatomic()
            }
        }
        AnimatedVisibility(visible = progressIndicator.first != data.id || !progressIndicator.second || data.id.isEmpty()) {
            UiIconWrapperSubatomic(
                modifier = Modifier.size(24.dp),
                icon = data.icon
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = data.label.asString(),
            style = DiiaTextStyle.t2TextDescription,
            color = Black
        )
    }
}

data class BtnLoadPlainIconAtmData(
    val actionKey: String = UIActionKeysCompose.BTN_LOAD_PLAIN_ICON_ATM,
    val componentId: UiText? = null,
    val id: String,
    val label: UiText,
    val icon: UiIcon,
    val action: DataActionWrapper? = null,
    val interactionState: UIState.Interaction = UIState.Interaction.Enabled
) : UIElementData

fun BtnLoadPlainIconAtm.toUIModel(id: String? = null): BtnLoadPlainIconAtmData {
    return BtnLoadPlainIconAtmData(
        componentId = UiText.DynamicString(this.componentId.orEmpty()),
        id = this.id ?: id ?: "",
        label = label.toDynamicString(),
        icon = UiIcon.DrawableResource(icon),
        interactionState = when (this.state) {
            ButtonStates.enabled -> UIState.Interaction.Enabled
            ButtonStates.disabled -> UIState.Interaction.Disabled
            ButtonStates.invisible -> UIState.Interaction.Disabled
            else -> UIState.Interaction.Enabled
        },
        action = action?.toDataActionWrapper()
    )
}


@Composable
@Preview
fun BtnLoadPlainIconAtm_Enabled() {
    val data = BtnLoadPlainIconAtmData(
        componentId = "component_id".toDynamicString(),
        id = "id",
        label = "Зареєструвати авто".toDynamicString(),
        icon = UiIcon.DrawableResource(DiiaResourceIcon.INFO.code),
        action = DataActionWrapper(
            type = "register",
            resource = "1234567890"
        ),
        interactionState = UIState.Interaction.Enabled
    )
    BtnLoadPlainIconAtm(data = data) {

    }
}

@Composable
@Preview
fun BtnLoadPlainIconAtm_Disabled() {
    val data = BtnLoadPlainIconAtmData(
        componentId = "component_id".toDynamicString(),
        id = "id",
        label = "Зареєструвати авто".toDynamicString(),
        icon = UiIcon.DrawableResource(DiiaResourceIcon.INFO.code),
        action = DataActionWrapper(
            type = "register",
            resource = "1234567890"
        ),
        interactionState = UIState.Interaction.Disabled
    )
    BtnLoadPlainIconAtm(data = data) {

    }
}

@Composable
@Preview
fun BtnLoadPlainIconAtm_Enabled_Long_Label() {
    val data = BtnLoadPlainIconAtmData(
        componentId = "component_id".toDynamicString(),
        id = "id",
        label = LoremIpsum(20).values.joinToString().toDynamicString(),
        icon = UiIcon.DrawableResource(DiiaResourceIcon.INFO.code),
        action = DataActionWrapper(
            type = "register",
            resource = "1234567890"
        ),
        interactionState = UIState.Interaction.Enabled
    )
    BtnLoadPlainIconAtm(data = data) {

    }
}

@Composable
@Preview
fun BtnLoadPlainIconAtm_Enabled_Long_Label_Loading() {
    val loadingState by remember { mutableStateOf("id" to false) }

    val data = BtnLoadPlainIconAtmData(
        componentId = "component_id".toDynamicString(),
        id = "id",
        label = LoremIpsum(20).values.joinToString().toDynamicString(),
        icon = UiIcon.DrawableResource(DiiaResourceIcon.INFO.code),
        action = DataActionWrapper(
            type = "register",
            resource = "1234567890"
        ),
        interactionState = UIState.Interaction.Enabled
    )
    BtnLoadPlainIconAtm(
        data = data,
        progressIndicator = loadingState
    ) {

    }
}
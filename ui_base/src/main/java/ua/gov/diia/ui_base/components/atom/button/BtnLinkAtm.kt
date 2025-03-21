package ua.gov.diia.ui_base.components.atom.button

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ua.gov.diia.core.models.common_compose.atm.button.BtnLinkAtm
import ua.gov.diia.core.models.common_compose.general.ButtonStates
import ua.gov.diia.ui_base.R
import ua.gov.diia.ui_base.components.infrastructure.DataActionWrapper
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.state.UIState
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.toDynamicString
import ua.gov.diia.ui_base.components.noRippleClickable
import ua.gov.diia.ui_base.components.theme.Black
import ua.gov.diia.ui_base.components.theme.BlackAlpha10
import ua.gov.diia.ui_base.util.toDataActionWrapper

@Composable
fun BtnLinkAtm(
    modifier: Modifier = Modifier,
    data: BtnLinkAtmData,
    onUIAction: (UIAction) -> Unit
) {
    Row(
        modifier = modifier
            .padding(horizontal = 24.dp)
            .padding(top = 24.dp)
            .fillMaxWidth()
            .noRippleClickable(debounce = true) {
                onUIAction(
                    UIAction(
                        actionKey = data.actionKey,
                        data = data.id,
                        action = data.action
                    )
                )
            }
            .testTag(data.componentId?.asString() ?: ""),
    ) {
        Text(
            text = AnnotatedString(data.title.asString()),
            textDecoration = TextDecoration.Underline,
            style = TextStyle(
                fontFamily = FontFamily(Font(R.font.e_ukraine_regular)),
                fontWeight = FontWeight.Normal,
                color = when (data.interactionState) {
                    UIState.Interaction.Disabled -> BlackAlpha10
                    UIState.Interaction.Enabled -> Black
                },
                textAlign = TextAlign.Center,
                fontSize = 11.sp,
                lineHeight = 16.sp
            )
        )
    }
}


data class BtnLinkAtmData(
    val componentId: UiText? = null,
    val actionKey: String = UIActionKeysCompose.BTN_LINK_ATM,
    val id: String = "",
    val title: UiText,
    val interactionState: UIState.Interaction = UIState.Interaction.Enabled,
    val action: DataActionWrapper? = null
) : UIElementData

fun BtnLinkAtm.toUIModel(
    id: String = "",
    componentIdExternal: UiText? = null
): BtnLinkAtmData {
    return BtnLinkAtmData(
        componentId = componentIdExternal ?: componentId.orEmpty().toDynamicString(),
        id = this.componentId ?: id,
        title = UiText.DynamicString(this.label),
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
fun BtnLinkAtmPreview_EnabledState() {
    val buttonStateEnabled = BtnLinkAtmData(
        title = UiText.DynamicString("Button Link"),
        interactionState = UIState.Interaction.Enabled
    )
    BtnLinkAtm(data = buttonStateEnabled) {
    }
}

@Composable
@Preview
fun BtnLinkAtmPreview_DisabledState() {
    val buttonStateDisabled = BtnLinkAtmData(
        title = UiText.DynamicString("Button Link"),
        id = "",
        interactionState = UIState.Interaction.Disabled
    )
    BtnLinkAtm(data = buttonStateDisabled) {
    }
}

package ua.gov.diia.ui_base.components.atom.button

import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.state.UIState
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.theme.SystemColor


@Composable
fun ButtonSystemAtom(
    modifier: Modifier = Modifier,
    data: ButtonSystemAtomData,
    onUIAction: (UIAction) -> Unit
) {
    TextButton(
        modifier = modifier
            .wrapContentSize()
            .alpha(
                when (data.interactionState) {
                    UIState.Interaction.Disabled -> 0.3f
                    UIState.Interaction.Enabled -> 1.0f
                }
            ),
        onClick = {
            onUIAction(UIAction(actionKey = data.actionKey, data = data.id))
        },
        enabled = data.interactionState == UIState.Interaction.Enabled
    ) {
        Text(
            text = data.title.asString().uppercase(),
            color = SystemColor,
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp,
            lineHeight = 16.sp
        )
    }
}


data class ButtonSystemAtomData(
    val actionKey: String = UIActionKeysCompose.BUTTON_REGULAR,
    val id: String = "",
    val title: UiText,
    val interactionState: UIState.Interaction = UIState.Interaction.Enabled
) : UIElementData

@Composable
@Preview
fun ButtonSystemAtomPreview_EnabledState() {
    val buttonStateEnabled = ButtonSystemAtomData(
        title = UiText.DynamicString("Label"),
        interactionState = UIState.Interaction.Enabled
    )
    ButtonSystemAtom(data = buttonStateEnabled) {}
}

@Composable
@Preview
fun ButtonSystemAtomPreview_DisabledState() {
    val buttonStateEnabled = ButtonSystemAtomData(
        title = UiText.DynamicString("Label"),
        interactionState = UIState.Interaction.Disabled
    )
    ButtonSystemAtom(data = buttonStateEnabled) {}
}
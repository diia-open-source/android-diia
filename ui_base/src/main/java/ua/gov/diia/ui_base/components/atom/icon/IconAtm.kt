package ua.gov.diia.ui_base.components.atom.icon

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.ui_base.components.DiiaResourceIcon
import ua.gov.diia.ui_base.components.infrastructure.DataActionWrapper
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.state.UIState
import ua.gov.diia.ui_base.components.noRippleClickable

@Composable
fun IconAtm(
    modifier: Modifier = Modifier,
    data: IconAtmData,
    onUIAction: (UIAction) -> Unit = {}
) {
    Image(
        modifier = modifier
            .size(32.dp)
            .noRippleClickable {
                if (data.interactionState == UIState.Interaction.Enabled) {
                    onUIAction(
                        UIAction(
                            actionKey = data.actionKey,
                            data = data.id,
                            action = data.action
                        )
                    )
                }
            }
            .semantics {
                testTag = data.componentId
            }
            .alpha(if (data.interactionState == UIState.Interaction.Disabled) 0.3f else 1f),
        painter = painterResource(
            id = DiiaResourceIcon.getResourceId(data.code)
        ),
        contentDescription = data.accessibilityDescription
    )
}

data class IconAtmData(
    val actionKey: String = UIActionKeysCompose.ICON_ATM_DATA,
    val id: String? = null,
    val componentId: String = "",
    val code: String,
    val accessibilityDescription: String? = null,
    val action: DataActionWrapper? = null,
    val interactionState: UIState.Interaction = UIState.Interaction.Enabled
)

@Preview
@Composable
fun IconAtmPreview() {
    val data = IconAtmData(
        id = "1",
        code = DiiaResourceIcon.MENU.code,
        accessibilityDescription = "Button"
    )
    IconAtm(data = data)
}

@Preview
@Composable
fun IconAtmPreview_disabled() {
    val data = IconAtmData(
        id = "1",
        code = DiiaResourceIcon.MENU.code,
        accessibilityDescription = "Button",
        interactionState = UIState.Interaction.Disabled
    )
    IconAtm(data = data)
}

@Preview
@Composable
fun IconAtmPreview_enabled() {
    val data = IconAtmData(
        id = "1",
        code = DiiaResourceIcon.MENU.code,
        accessibilityDescription = "Button",
        interactionState = UIState.Interaction.Enabled
    )
    IconAtm(data = data)
}


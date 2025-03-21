package ua.gov.diia.ui_base.components.atom.icon

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.core.models.common_compose.atm.icon.SmallIconAtm
import ua.gov.diia.ui_base.components.DiiaResourceIcon
import ua.gov.diia.ui_base.components.infrastructure.DataActionWrapper
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.toDynamicStringOrNull
import ua.gov.diia.ui_base.components.noRippleClickable
import ua.gov.diia.ui_base.util.toDataActionWrapper

@Composable
fun SmallIconAtm(
    modifier: Modifier = Modifier,
    data: SmallIconAtmData,
    onUIAction: (UIAction) -> Unit = {}
) {
    Image(
        modifier = modifier
            .size(24.dp)
            .noRippleClickable {
                onUIAction(
                    UIAction(
                        actionKey = data.actionKey,
                        data = data.id,
                        action = data.action
                    )
                )
            }
            .testTag(data.componentId?.asString() ?: ""),
        painter = painterResource(
            id = DiiaResourceIcon.getResourceId(data.code)
        ),
        contentDescription = data.accessibilityDescription
    )
}

data class SmallIconAtmData(
    val componentId: UiText? = null,
    val actionKey: String = UIActionKeysCompose.SMALL_ICON_ATM_DATA,
    val id: String? = null,
    val code: String,
    val accessibilityDescription: String? = null,
    val action: DataActionWrapper? = null
)

fun SmallIconAtm.toUiModel(id: String? = null): SmallIconAtmData {
    return SmallIconAtmData(
        componentId = componentId.toDynamicStringOrNull(),
        id = id,
        code = code,
        accessibilityDescription = accessibilityDescription,
        action = action?.toDataActionWrapper()
    )
}

@Preview
@Composable
fun SmallIconAtmPreview() {
    val data = SmallIconAtmData(
        id = "1",
        code = DiiaResourceIcon.MENU.code,
        accessibilityDescription = "Button"
    )
    SmallIconAtm(data = data)
}


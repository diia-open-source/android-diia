package ua.gov.diia.ui_base.components.atom.icon

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.core.models.common_compose.atm.icon.LargeIconAtm
import ua.gov.diia.ui_base.components.DiiaResourceIcon
import ua.gov.diia.ui_base.components.infrastructure.DataActionWrapper
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.noRippleClickable
import ua.gov.diia.ui_base.util.toDataActionWrapper

@Composable
fun LargeIconAtm(
    modifier: Modifier = Modifier,
    data: LargeIconAtmData,
    onUIAction: (UIAction) -> Unit = {}
) {
    Image(
        modifier = modifier
            .size(68.dp, 28.dp)
            .noRippleClickable {
                onUIAction(
                    UIAction(
                        actionKey = data.actionKey,
                        data = data.id,
                        action = data.action
                    )
                )
            },
        painter = painterResource(
            id = DiiaResourceIcon.getResourceId(data.code)
        ),
        contentDescription = data.accessibilityDescription
    )
}

data class LargeIconAtmData(
    val actionKey: String = UIActionKeysCompose.LARGE_ICON_ATM_DATA,
    val id: String? = null,
    val componentId: String? = null,
    val code: String,
    val accessibilityDescription: String? = null,
    val action: DataActionWrapper? = null
)

fun LargeIconAtm.toUiModel(id: String? = null): LargeIconAtmData {
    return LargeIconAtmData(
        id = id,
        componentId = componentId.orEmpty(),
        code = code,
        accessibilityDescription = accessibilityDescription,
        action = action?.toDataActionWrapper()
    )
}

@Preview
@Composable
fun LargeIconAtmPreview() {
    val data = LargeIconAtmData(
        id = "1",
        code = DiiaResourceIcon.MADE_IN_UA.code,
        accessibilityDescription = "Button"
    )
    LargeIconAtm(data = data)
}


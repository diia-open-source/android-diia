package ua.gov.diia.ui_base.components.atom.icon

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.ui_base.components.DiiaResourceIcon
import ua.gov.diia.ui_base.components.infrastructure.DataActionWrapper
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.noRippleClickable

@Composable
fun DoubleIconAtm(
    modifier: Modifier = Modifier,
    data: DoubleIconAtmData,
    onUIAction: (UIAction) -> Unit = {}
) {
    Image(
        modifier = modifier
            .size(width = 58.dp, height = 32.dp)
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

data class DoubleIconAtmData(
    val actionKey: String = UIActionKeysCompose.DOUBLE_ICON_ATM_DATA,
    val id: String? = null,
    val code: String,
    val accessibilityDescription: String? = null,
    val action: DataActionWrapper? = null
)

@Preview
@Composable
fun DoubleIconAtmPreview() {
    val data = DoubleIconAtmData(
        id = "1",
        code = DiiaResourceIcon.SAFETY.code,
        accessibilityDescription = "Button"
    )
    DoubleIconAtm(data = data)
}


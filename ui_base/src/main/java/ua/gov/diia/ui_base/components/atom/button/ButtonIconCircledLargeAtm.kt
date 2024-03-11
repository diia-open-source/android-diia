package ua.gov.diia.ui_base.components.atom.button

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.ui_base.R
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.noRippleClickable
import ua.gov.diia.ui_base.components.subatomic.icon.IconWithBadge
import ua.gov.diia.ui_base.components.theme.DiiaTextStyle

@Composable
fun ButtonIconCircledLargeAtm(
    modifier: Modifier = Modifier,
    data: ButtonIconCircledLargeAtmData,
    onUIAction: (UIAction) -> Unit
) {
    Column(
        modifier = modifier
            .noRippleClickable {
                onUIAction(
                    UIAction(
                        actionKey = data.actionKey,
                        data = data.id,
                    )
                )
            },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        data.icon?.let {
            IconWithBadge(
                modifier = Modifier
                    .size(52.dp),
                image = it,
            )
        }

        data.label?.let {
            Text(
                modifier = Modifier.padding(top = 12.dp),
                text = data.label,
                style = DiiaTextStyle.t1BigText,
                color = Color.Black
            )
        }
    }
}

data class ButtonIconCircledLargeAtmData(
    val actionKey: String = UIActionKeysCompose.BUTTON_CIRCLED,
    val id: String? = "",
    val label: String? = null,
    val icon: UiText? = null,
) : UIElementData

@Composable
@Preview
fun ButtonIconCircledLargeAtmPreview() {
    val state = ButtonIconCircledLargeAtmData(
        label = "Label",
        icon = UiText.StringResource(R.drawable.ic_doc_qr_selected),
    )
    ButtonIconCircledLargeAtm(modifier = Modifier, data = state) {}
}
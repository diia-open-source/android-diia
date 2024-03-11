package ua.gov.diia.ui_base.components.atom.button

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.ui_base.R
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.state.UIState
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.noRippleClickable
import ua.gov.diia.ui_base.components.subatomic.icon.IconWithBadge

@Composable
fun BtnIconCircledWhiteAtm(
    modifier: Modifier = Modifier,
    data: BtnIconCircledWhiteAtmData,
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
                    .size(40.dp),
                image = it,
            )
        }
    }
}

data class BtnIconCircledWhiteAtmData(
    val actionKey: String = UIActionKeysCompose.BUTTON_CIRCLED,
    val id: String? = "",
    val icon: UiText? = null,
    val interactionState: UIState.Interaction
) : UIElementData

@Composable
@Preview
fun BtnIconCircledAtmPreview() {
    val state = BtnIconCircledWhiteAtmData(
        icon = UiText.StringResource(R.drawable.ic_btn_doc_scan_close),
        interactionState = UIState.Interaction.Enabled
    )
    BtnIconCircledWhiteAtm(modifier = Modifier, data = state) {}
}
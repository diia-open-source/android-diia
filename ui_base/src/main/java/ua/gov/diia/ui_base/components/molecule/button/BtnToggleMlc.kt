package ua.gov.diia.ui_base.components.molecule.button

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
import ua.gov.diia.ui_base.components.infrastructure.state.UIState
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.noRippleClickable
import ua.gov.diia.ui_base.components.subatomic.icon.IconWithBadge
import ua.gov.diia.ui_base.components.theme.DiiaTextStyle

@Composable
fun BtnToggleMlc(
    modifier: Modifier = Modifier,
    data: BtnToggleMlcData,
    onUIAction: (UIAction) -> Unit
) {
    Column(
        modifier = modifier
            .noRippleClickable {
                onUIAction(
                    UIAction(
                        actionKey = data.actionKey,
                        data = data.id,
                        states = listOf(UIState.Selection.Selected)
                    )
                )
            },
        horizontalAlignment = Alignment.CenterHorizontally
        ) {

        IconWithBadge(
            modifier = Modifier
                .size(52.dp),
            image = when (data.selectionState) {
                UIState.Selection.Selected -> data.iconSelected
                    ?: UiText.StringResource(resId = R.drawable.diia_check)

                UIState.Selection.Unselected -> data.iconUnselected ?: UiText.StringResource(
                    resId = R.drawable.diia_back_arrow
                )
            },
        )

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

data class BtnToggleMlcData(
    val actionKey: String = UIActionKeysCompose.TOGGLE_BUTTON_MOLECULE,
    val id: String? = "",
    val label: String? = null,
    val iconSelected: UiText? = null,
    val iconUnselected: UiText? = null,
    val selectionState: UIState.Selection = UIState.Selection.Unselected
) : UIElementData

@Composable
@Preview
fun BtnToggleMlcSelected() {
    val state = BtnToggleMlcData(
        label = "Label",
        iconSelected = UiText.StringResource(R.drawable.ic_doc_qr_selected),
        iconUnselected = UiText.StringResource(R.drawable.ic_doc_qr_unselected),
        selectionState = UIState.Selection.Selected
    )
    BtnToggleMlc(modifier = Modifier, data = state) {}
}

@Composable
@Preview
fun BtnToggleMlcUnselected() {
    val state = BtnToggleMlcData(
        label = "Label",
        iconSelected = UiText.StringResource(R.drawable.ic_doc_qr_selected),
        iconUnselected = UiText.StringResource(R.drawable.ic_doc_qr_unselected),
        selectionState = UIState.Selection.Unselected
    )
    BtnToggleMlc(modifier = Modifier, data = state) {}
}
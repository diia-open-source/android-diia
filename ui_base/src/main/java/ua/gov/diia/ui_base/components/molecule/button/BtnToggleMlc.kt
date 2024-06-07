package ua.gov.diia.ui_base.components.molecule.button

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.core.models.common_compose.mlc.button.BtnToggleMlc
import ua.gov.diia.ui_base.components.DiiaResourceIcon
import ua.gov.diia.ui_base.components.infrastructure.DataActionWrapper
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.state.UIState
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiIcon
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.toDynamicString
import ua.gov.diia.ui_base.components.noRippleClickable
import ua.gov.diia.ui_base.components.theme.Black
import ua.gov.diia.ui_base.components.theme.BlackAlpha10
import ua.gov.diia.ui_base.components.theme.DiiaTextStyle
import ua.gov.diia.ui_base.util.toDataActionWrapper

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
                        action = data.action,
                        states = listOf(UIState.Selection.Selected)
                    )
                )
            }.testTag(data.componentId?.asString() ?: ""),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(52.dp)
                .background(
                    when (data.selectionState) {
                        UIState.Selection.Selected -> Black
                        UIState.Selection.Unselected -> BlackAlpha10
                    },
                    shape = RoundedCornerShape(40.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Image(
                modifier = modifier
                    .size(32.dp)
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
                    id = DiiaResourceIcon.getResourceId(
                        if (data.iconSelected != null && data.iconSelected is UiIcon.DrawableResource && data.iconUnselected != null && data.iconUnselected is UiIcon.DrawableResource) {
                            when (data.selectionState) {
                                UIState.Selection.Selected -> data.iconSelected.code
                                UIState.Selection.Unselected -> data.iconUnselected.code
                            }
                        } else {
                            ""
                        }
                    )
                ),
                contentDescription = ""
            )
        }

        data.label?.let {
            Text(
                modifier = Modifier.padding(top = 12.dp),
                text = data.label.asString(),
                style = DiiaTextStyle.t1BigText,
                color = Color.Black
            )
        }
    }
}

data class BtnToggleMlcData(
    val actionKey: String = UIActionKeysCompose.TOGGLE_BUTTON_MOLECULE,
    val componentId: UiText? = null,
    val id: String? = "",
    val label: UiText? = null,
    val iconSelected: UiIcon? = null,
    val iconUnselected: UiIcon? = null,
    val action: DataActionWrapper,
    val selectionState: UIState.Selection = UIState.Selection.Unselected
) : UIElementData

fun BtnToggleMlc.toUIModel(selectionState: UIState.Selection = UIState.Selection.Unselected): UIElementData {
    return BtnToggleMlcData(
        componentId = this.componentId.orEmpty().toDynamicString(),
        id = this.code,
        iconSelected = UiIcon.DrawableResource(this.selected.icon),
        iconUnselected = UiIcon.DrawableResource(this.notSelected.icon),
        label = this.label.let {
            it.toDynamicString()
        },
        selectionState = selectionState,
        action = this.selected.action.toDataActionWrapper()
    )
}

@Composable
@Preview
fun BtnToggleMlcSelected() {
    val state = BtnToggleMlcData(
        label = "Label".toDynamicString(),
        iconSelected = UiIcon.DrawableResource(DiiaResourceIcon.QR_WHITE.code),
        iconUnselected = UiIcon.DrawableResource(DiiaResourceIcon.QR.code),
        action = DataActionWrapper(
            type = "qr",
            subtype = null,
            resource = ""
        ),
        selectionState = UIState.Selection.Selected
    )
    BtnToggleMlc(modifier = Modifier, data = state) {}
}

@Composable
@Preview
fun BtnToggleMlcUnselected() {
    val state = BtnToggleMlcData(
        label = "Label".toDynamicString(),
        iconSelected = UiIcon.DrawableResource(DiiaResourceIcon.QR_WHITE.code),
        iconUnselected = UiIcon.DrawableResource(DiiaResourceIcon.QR.code),
        action = DataActionWrapper(
            type = "qr",
            subtype = null,
            resource = ""
        ),
        selectionState = UIState.Selection.Unselected
    )
    BtnToggleMlc(modifier = Modifier, data = state) {}
}
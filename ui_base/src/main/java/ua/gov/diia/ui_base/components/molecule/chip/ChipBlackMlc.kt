package ua.gov.diia.ui_base.components.molecule.chip

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.core.models.common_compose.mlc.chip.ChipBlackMlc
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose.CHIP_BLACK_MLC
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose.CHIP_MLC
import ua.gov.diia.ui_base.components.infrastructure.state.UIState
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.toDynamicString
import ua.gov.diia.ui_base.components.noRippleClickable
import ua.gov.diia.ui_base.components.theme.Black
import ua.gov.diia.ui_base.components.theme.BlackAlpha20
import ua.gov.diia.ui_base.components.theme.BlackAlpha30
import ua.gov.diia.ui_base.components.theme.DiiaTextStyle
import ua.gov.diia.ui_base.components.theme.Primary
import ua.gov.diia.ui_base.components.theme.White

@Composable
fun ChipBlackMlc(
    modifier: Modifier = Modifier,
    data: ChipBlackMlcData,
    onUIAction: (UIAction) -> Unit
) {
    val isActive = data.active
    val isSelected = data.selectionState == UIState.Selection.Selected

    val backgroundModifier = if (isActive && isSelected) {
        Modifier
            .background(color = Black, shape = RoundedCornerShape(40.dp))
    } else if (!isActive && isSelected) {
        Modifier
            .background(color = White, shape = RoundedCornerShape(40.dp))
            .border(width = 1.dp, color = BlackAlpha30, shape = RoundedCornerShape(40.dp))
    } else {
        Modifier
            .background(color = White, shape = RoundedCornerShape(40.dp))
            .border(width = 1.dp, color = BlackAlpha20, shape = RoundedCornerShape(40.dp))
    }

    Box(
        modifier = modifier
            .then(backgroundModifier)
            .noRippleClickable {
                if(data.active){
                    onUIAction(
                        UIAction(
                            actionKey = data.actionKey ?: CHIP_BLACK_MLC,
                            data = data.id ?: data.componentId,
                            states = listOf(UIState.Selection.Selected)
                        )
                    )
                }
            }
            .testTag(data.componentId ?: "")
    ) {
        Text(
            modifier = Modifier
                .padding(vertical = 11.dp)
                .padding(horizontal = 18.dp),
            text = data.label.asString(),
            color = if (isSelected && isActive) White
                else if (!isSelected && isActive) Black else BlackAlpha20,
            style = DiiaTextStyle.t2TextDescription
        )
    }
}

data class ChipBlackMlcData(
    val componentId: String? = null,
    val actionKey: String? = CHIP_BLACK_MLC,
    val id: String? = null,
    val label: UiText,
    val code: String,
    val active: Boolean = true,
    val selectionState: UIState.Selection = UIState.Selection.Unselected,
) : UIElementData

fun ChipBlackMlc.toUiModel(): ChipBlackMlcData {
    return ChipBlackMlcData(
        componentId = componentId.orEmpty(),
        id = componentId.orEmpty(),
        label = label.toDynamicString(),
        code = code,
        active = active ?: true,
//        selectionState = if (code == preselectedCode) {
//            UIState.Selection.Selected
//        } else {
//            UIState.Selection.Unselected
//        }
    )
}

@Composable
@Preview
fun ChipBlackMlcPreview_Active_Selected() {
    val data = ChipBlackMlcData(
        label = UiText.DynamicString("label"),
        code = "inProgress",
        active = true,
        selectionState = UIState.Selection.Selected)
    Box(modifier = Modifier.background(Primary)) {
        ChipBlackMlc(
            data = data
        ) {}
    }
}

@Composable
@Preview
fun ChipBlackMlcPreview_Active_Unselected() {
    val data = ChipBlackMlcData(
        label = UiText.DynamicString("label"),
        code = "inProgress",
        active = true,
        selectionState = UIState.Selection.Unselected)
    Box(modifier = Modifier.background(Primary)) {
        ChipBlackMlc(
            data = data
        ) {}
    }
}

@Composable
@Preview
fun ChipBlackMlcPreview_NotActive_Selected() {
    val data = ChipBlackMlcData(
        label = UiText.DynamicString("label"),
        code = "inProgress",
        active = false,
        selectionState = UIState.Selection.Unselected)
    Box(modifier = Modifier.background(Primary)) {
        ChipBlackMlc(
            data = data
        ) {}
    }
}
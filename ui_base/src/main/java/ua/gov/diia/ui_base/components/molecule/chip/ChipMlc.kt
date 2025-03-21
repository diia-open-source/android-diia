package ua.gov.diia.ui_base.components.molecule.chip

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.core.models.common_compose.atm.icon.BadgeCounterAtm
import ua.gov.diia.core.models.common_compose.mlc.chip.ChipMlc
import ua.gov.diia.ui_base.components.DiiaResourceIcon
import ua.gov.diia.ui_base.components.infrastructure.DataActionWrapper
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose.CHIP_MLC
import ua.gov.diia.ui_base.components.infrastructure.state.UIState
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiIcon
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.toDynamicString
import ua.gov.diia.ui_base.components.noRippleClickable
import ua.gov.diia.ui_base.components.subatomic.icon.BadgeSubatomic
import ua.gov.diia.ui_base.components.subatomic.icon.UiIconWrapperSubatomic
import ua.gov.diia.ui_base.components.theme.DiiaTextStyle
import ua.gov.diia.ui_base.components.theme.Primary
import ua.gov.diia.ui_base.components.theme.White
import ua.gov.diia.ui_base.components.theme.WhiteAlpha30
import ua.gov.diia.ui_base.util.toDataActionWrapper

@Composable
fun ChipMlc(
    modifier: Modifier = Modifier,
    data: ChipMlcData,
    onUIAction: (UIAction) -> Unit
) {
    Row(
        modifier = modifier
            .background(
                color = if (data.selectionState == UIState.Selection.Selected) {
                    White
                } else {
                    WhiteAlpha30
                }, shape = RoundedCornerShape(40.dp)
            )
            .noRippleClickable {
                onUIAction(
                    UIAction(
                        actionKey = data.actionKey ?: CHIP_MLC,
                        data = data.id,
                        action = data.action,
                        states = listOf(UIState.Selection.Selected)
                    )
                )
            }
            .testTag(data.componentId ?: ""),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier
                .padding(vertical = 11.dp)
                .padding(
                    start = 18.dp, end = if (
                        data.selectionState == UIState.Selection.Selected &&
                        (data.selectedIcon != null || data.badgeCounterAtm != null)
                    ) {
                        6.dp
                    } else {
                        18.dp
                    }
                ),
            text = data.label.asString(),
            style = DiiaTextStyle.t2TextDescription
        )
        if (data.selectionState == UIState.Selection.Selected) {
            data.selectedIcon?.let {
                UiIconWrapperSubatomic(
                    modifier = Modifier.size(18.dp),
                    icon = data.selectedIcon
                )
                Spacer(modifier = Modifier.width(12.dp))
            }
        }

        data.badgeCounterAtm?.let {
            if (it.count > 0) {
                BadgeSubatomic(value = it.count)
            }
            Spacer(modifier = Modifier.width(12.dp))
        }
    }
}

data class ChipMlcData(
    val componentId: String? = null,
    val actionKey: String? = CHIP_MLC,
    val id: String = "",
    val label: UiText,
    val code: String,
    val badgeCounterAtm: BadgeCounterAtm? = null,
    val active: Boolean? = null,
    val selectedIcon: UiIcon? = null,
    val selectionState: UIState.Selection = UIState.Selection.Unselected,
    val action: DataActionWrapper? = null
) : UIElementData

fun ChipMlc.toUIModel(selectionState: UIState.Selection = UIState.Selection.Unselected): ChipMlcData {
    return ChipMlcData(
        componentId = componentId.orEmpty(),
        id = code,
        label = label.toDynamicString(),
        code = code,
        badgeCounterAtm = badgeCounterAtm,
        active = active,
        selectedIcon = selectedIcon?.let { icon ->
            UiIcon.DrawableResource(code = icon)
        },
        action = action?.toDataActionWrapper(),
        selectionState = selectionState
    )
}

@Composable
@Preview
fun ChipMlcPreview_Selected_Without_Icon() {
    val data = ChipMlcData(
        label = UiText.DynamicString("label"),
        code = "inProgress",
        selectedIcon = null,
        selectionState = UIState.Selection.Selected
    )
    Box(modifier = Modifier.background(Primary)) {
        ChipMlc(
            data = data
        ) {}
    }
}

@Composable
@Preview
fun ChipMlcPreview_Selected() {
    val data = ChipMlcData(
        label = UiText.DynamicString("label"),
        code = "inProgress",
        selectedIcon = UiIcon.DrawableResource(DiiaResourceIcon.ELLIPSE_CHECK.code),
        selectionState = UIState.Selection.Selected
    )
    Box(modifier = Modifier.background(Primary)) {
        ChipMlc(
            data = data
        ) {}
    }
}

@Composable
@Preview
fun ChipMlcPreview_WithCounter() {
    val data = ChipMlcData(
        label = UiText.DynamicString("label"),
        code = "pending",
        badgeCounterAtm = BadgeCounterAtm(count = 1),
        selectionState = UIState.Selection.Selected
    )
    Box(modifier = Modifier.background(Primary)) {
        ChipMlc(
            data = data
        ) {}
    }
}

@Composable
@Preview
fun ChipMlcPreview_WithCounter_Zero() {
    val data = ChipMlcData(
        label = UiText.DynamicString("label"),
        code = "pending",
        badgeCounterAtm = BadgeCounterAtm(count = 0),
        selectionState = UIState.Selection.Selected
    )
    Box(modifier = Modifier.background(Primary)) {
        ChipMlc(
            data = data
        ) {}
    }
}


@Composable
@Preview
fun ChipMlcPreview_IconWithCounter() {
    val data = ChipMlcData(
        label = UiText.DynamicString("label"),
        code = "pending",
        selectedIcon = UiIcon.DrawableResource(DiiaResourceIcon.ELLIPSE_CHECK.code),
        badgeCounterAtm = BadgeCounterAtm(count = 1),
        selectionState = UIState.Selection.Selected
    )
    Box(modifier = Modifier.background(Primary)) {
        ChipMlc(
            data = data
        ) {}
    }
}

@Composable
@Preview
fun ChipMlcPreview_Unselected() {
    val data = ChipMlcData(
        label = UiText.DynamicString("label"),
        code = "pending",
        badgeCounterAtm = BadgeCounterAtm(count = 11),
        selectionState = UIState.Selection.Unselected
    )
    Box(modifier = Modifier.background(Primary)) {
        ChipMlc(
            data = data
        ) {}
    }
}
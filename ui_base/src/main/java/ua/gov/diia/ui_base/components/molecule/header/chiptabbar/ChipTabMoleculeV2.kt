package ua.gov.diia.ui_base.components.molecule.header.chiptabbar

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.ui_base.R
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose.CHIP_TABS_MOLECULE
import ua.gov.diia.ui_base.components.infrastructure.state.UIState
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.noRippleClickable
import ua.gov.diia.ui_base.components.theme.DiiaTextStyle
import ua.gov.diia.ui_base.components.theme.GrannySmithApple
import ua.gov.diia.ui_base.components.theme.White
import ua.gov.diia.ui_base.components.theme.WhiteAlpha30

@Composable
fun ChipTabMoleculeV2(
    modifier: Modifier = Modifier,
    data: ChipTabMoleculeDataV2,
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
                        actionKey = data.actionKey ?: CHIP_TABS_MOLECULE,
                        data = data.id,
                        states = listOf(UIState.Selection.Selected)
                    )
                )
            }
            .testTag(data.componentId?.asString() ?: ""),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier
                .padding(vertical = 11.dp)
                .padding(
                    start = 18.dp, end = if (data.selectionState == UIState.Selection.Selected) {
                        6.dp
                    } else {
                        18.dp
                    }
                ),
            text = data.title,
            style = DiiaTextStyle.t2TextDescription
        )
        if (data.selectionState == UIState.Selection.Selected) {
            Image(
                modifier = Modifier,
                painter = painterResource(id = R.drawable.ic_chip_check),
                contentDescription = "badgeIcon"
            )
            Spacer(modifier = Modifier.width(12.dp))
        }

    }

}

data class ChipTabMoleculeDataV2(
    val actionKey: String? = CHIP_TABS_MOLECULE,
    val id: String = "",
    val title: String,
    val selectionState: UIState.Selection = UIState.Selection.Unselected,
    val componentId: UiText? = null
) : UIElementData

@Composable
@Preview
fun ChipTabMoleculeV2Preview_Selected() {
    val data = ChipTabMoleculeDataV2(title = "Label", selectionState = UIState.Selection.Selected)
    Box(modifier = Modifier.background(GrannySmithApple)) {
        ChipTabMoleculeV2(
            data = data
        ) {}
    }
}

@Composable
@Preview
fun ChipTabMoleculeV2Preview_Unselected() {
    val data = ChipTabMoleculeDataV2(title = "Label", selectionState = UIState.Selection.Unselected)
    Box(modifier = Modifier.background(GrannySmithApple)) {
        ChipTabMoleculeV2(
            data = data
        ) {}
    }
}
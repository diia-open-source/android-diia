package ua.gov.diia.ui_base.components.molecule.header.chiptabbar

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose.CHIP_TABS_MOLECULE
import ua.gov.diia.ui_base.components.infrastructure.state.UIState
import ua.gov.diia.ui_base.components.subatomic.icon.BadgeSubatomic
import ua.gov.diia.ui_base.components.theme.DiiaTextStyle
import ua.gov.diia.ui_base.components.theme.GrannySmithApple
import ua.gov.diia.ui_base.components.theme.White
import ua.gov.diia.ui_base.components.theme.WhiteAlpha30

@Composable
fun ChipTabMolecule(
    modifier: Modifier = Modifier,
    data: ChipTabMoleculeData,
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
            .clickable {
                onUIAction(
                    UIAction(
                        actionKey = data.actionKey ?: CHIP_TABS_MOLECULE,
                        data = data.id,
                        states = listOf(UIState.Selection.Selected)
                    )
                )
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier
                .padding(vertical = 11.dp)
                .padding(
                    start = 18.dp, end = if (data.counter != 0) {
                        6.dp
                    } else {
                        18.dp
                    }
                ),
            text = data.title,
            style = DiiaTextStyle.t1BigText
        )
        if (data.counter != 0) {
            BadgeSubatomic(value = data.counter)
            Spacer(modifier = Modifier.width(12.dp))
        }

    }

}

data class ChipTabMoleculeData(
    val actionKey: String? = CHIP_TABS_MOLECULE,
    val id: String = "",
    val title: String,
    val counter: Int = 0,
    val selectionState: UIState.Selection = UIState.Selection.Unselected
) : UIElementData

@Composable
@Preview
fun ChipTabMoleculePreview_Selected_WithoutCounter() {
    val data = ChipTabMoleculeData(title = "Label")
    Box(modifier = Modifier.background(GrannySmithApple)) {
        ChipTabMolecule(
            modifier = Modifier.padding(16.dp),
            data = data
        ) {

        }
    }

}

@Composable
@Preview
fun ChipTabMoleculePreview_Selected_WithCounter() {
    val data = ChipTabMoleculeData(title = "Label", counter = 1)
    Box(modifier = Modifier.background(GrannySmithApple)) {
        ChipTabMolecule(
            modifier = Modifier.padding(16.dp),
            data = data
        ) {

        }
    }

}

@Composable
@Preview
fun ChipTabMoleculePreview_Unselected_WithCounter() {
    val data = ChipTabMoleculeData(title = "Label", counter = 2, selectionState = UIState.Selection.Unselected)
    Box(modifier = Modifier.background(GrannySmithApple)) {
        ChipTabMolecule(
            modifier = Modifier.padding(16.dp),
            data = data
        ) {

        }
    }

}

@Composable
@Preview
fun ChipTabMoleculePreview_Unselected_WithoutCounter() {
    val data = ChipTabMoleculeData(title = "Label", selectionState = UIState.Selection.Unselected)
    Box(modifier = Modifier.background(GrannySmithApple)) {
        ChipTabMolecule(
            modifier = Modifier.padding(16.dp),
            data = data
        ) {

        }
    }

}
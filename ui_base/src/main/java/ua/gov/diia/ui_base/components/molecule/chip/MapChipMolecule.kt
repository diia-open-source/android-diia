package ua.gov.diia.ui_base.components.molecule.chip

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.ui_base.R
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.state.UIState
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.theme.Black
import ua.gov.diia.ui_base.components.theme.DiiaTextStyle
import ua.gov.diia.ui_base.components.theme.White

@Composable
fun MapChipMolecule(
    modifier: Modifier = Modifier,
    data: MapChipMoleculeData,
    onUIAction: (UIAction) -> Unit
) {
    val color = if (data.selection == UIState.Selection.Selected) Black else White
    val textColor = if (data.selection == UIState.Selection.Selected) White else Black
    Row(
        modifier = modifier
            .wrapContentSize()
            .background(color, RoundedCornerShape(40.dp))
            .padding(
                start = 6.dp,
                top = 6.dp,
                bottom = 6.dp,
            )
            .clickable { onUIAction.invoke(UIAction(data.actionKey, data = data.id)) },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            modifier = Modifier.height(24.dp),
            painter = painterResource(data.iconsRes),
            contentDescription = ""
        )

        Text(
            modifier = modifier.padding(end = 16.dp, start = 8.dp),
            text = data.title.asString(),
            style = DiiaTextStyle.t2TextDescription,
            color = textColor
        )
    }
}

data class MapChipMoleculeData(
    val id: String,
    val actionKey: String,
    val iconsRes: Int,
    val title: UiText,
    val selection: UIState.Selection = UIState.Selection.Unselected
)

@Composable
@Preview
fun MapChipMoleculePreview_shelter() {
    val data = MapChipMoleculeData(
        id = "id1",
        actionKey = "actionKey",
        iconsRes = R.drawable.ic_chip_shelter,
        title = UiText.DynamicString("Укриття")
    )
    MapChipMolecule(data = data) {}
}

@Composable
@Preview
fun MapChipMoleculePreview_shelter_selected() {
    val data = MapChipMoleculeData(
        id = "id2",
        actionKey = "actionKey",
        iconsRes = R.drawable.ic_chip_shelter,
        title = UiText.DynamicString("Укриття"),
        selection = UIState.Selection.Selected
    )
    MapChipMolecule(data = data) {}
}


@Composable
@Preview
fun MapChipMoleculePreview_points() {
    val data = MapChipMoleculeData(
        id = "id1",
        actionKey = "actionKey",
        iconsRes = R.drawable.ic_chip_point,
        title = UiText.DynamicString("Пункти незламності")
    )
    MapChipMolecule(data = data) {}
}

@Composable
@Preview
fun MapChipMoleculePreview_points_selected() {
    val data = MapChipMoleculeData(
        id = "id1",
        actionKey = "actionKey",
        iconsRes = (R.drawable.ic_chip_point),
        title = UiText.DynamicString("Пункти незламності"),
        selection = UIState.Selection.Selected
    )
    MapChipMolecule(data = data) {}
}

@Composable
@Preview
fun MapChipMoleculePreview_points_shelter() {
    val data = MapChipMoleculeData(
        id = "id1",
        actionKey = "actionKey",
        iconsRes = (R.drawable.ic_chip_all),
        title = UiText.DynamicString("Всі")
    )
    MapChipMolecule(data = data) {}
}

@Composable
@Preview
fun MapChipMoleculePreview_points_shelter_selected() {
    val data = MapChipMoleculeData(
        id = "id1",
        actionKey = "actionKey",
        iconsRes = R.drawable.ic_chip_all,
        title = UiText.DynamicString("Всі"),
        selection = UIState.Selection.Selected
    )
    MapChipMolecule(data = data) {}
}
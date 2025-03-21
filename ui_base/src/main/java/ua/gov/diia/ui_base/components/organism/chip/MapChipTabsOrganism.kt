package ua.gov.diia.ui_base.components.organism.chip

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.core.models.common_compose.org.chip.MapChipTabsOrg
import ua.gov.diia.ui_base.R
import ua.gov.diia.ui_base.components.DiiaResourceIcon
import ua.gov.diia.ui_base.components.atom.button.BtnWhiteAdditionalIconAtm
import ua.gov.diia.ui_base.components.atom.button.BtnWhiteAdditionalIconAtmData
import ua.gov.diia.ui_base.components.atom.icon.BadgeCounterAtmData
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.state.UIState
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiIcon
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.molecule.chip.MapChipMolecule
import ua.gov.diia.ui_base.components.molecule.chip.MapChipMoleculeData


@Composable
fun MapChipTabsOrganism(
    modifier: Modifier = Modifier,
    data: MapChipTabsOrganismData,
    onUIAction: (UIAction) -> Unit
) {
    val listState = rememberLazyListState()
    val selectedIndex = data.chips.indexOfLast { it.selection == UIState.Selection.Selected }
    LaunchedEffect(key1 = selectedIndex) {
        if (selectedIndex != -1) {
            listState.animateScrollToItem(selectedIndex)
        }
    }
    LazyRow(
        modifier = modifier,
        state = listState,
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        itemsIndexed(items = data.chips) { index, item ->
            if (index == 0 &&  data.btn != null) {
                BtnWhiteAdditionalIconAtm(data = data.btn, onUIAction = onUIAction)
                Spacer(modifier = Modifier.width(8.dp))
            }

            MapChipMolecule(data = item, onUIAction = onUIAction)
            if (index < data.chips.size - 1) {
                Spacer(modifier = Modifier.width(8.dp))
            }
        }
    }
}

data class MapChipTabsOrganismData(
    val chips: List<MapChipMoleculeData>,
    val btn: BtnWhiteAdditionalIconAtmData?
) : UIElementData {

    fun onChipSelected(id: String): MapChipTabsOrganismData {
        val chips = chips.map {
            it.copy(selection = if (it.id == id) UIState.Selection.Selected else UIState.Selection.Unselected)
        }
        return MapChipTabsOrganismData(chips, btn)
    }
}

fun MapChipTabsOrg.toUiModel(): MapChipTabsOrganismData {
    val chips = mutableListOf<MapChipMoleculeData>()
    this.items.mapNotNull { it.chipMlc }.forEach { e ->
        val selected = if (this.preselectedCode == e.code)
            UIState.Selection.Selected
        else
            UIState.Selection.Unselected

        chips.add(
            MapChipMoleculeData(
                id = e.code,
                actionKey = e.code,
                iconsRes = DiiaResourceIcon.getResourceId(e.icon),
                title = UiText.DynamicString(e.label),
                selection = selected
            )
        )
    }

    return MapChipTabsOrganismData(chips, null)
}

@Preview
@Composable
fun MapChipTabsOrganismPreview() {
    val dataP = MapChipMoleculeData(
        id = "id1",
        actionKey = "actionKey",
        iconsRes = (R.drawable.ic_chip_point),
        title = UiText.DynamicString("Пункти незламності")
    )

    val dataS = MapChipMoleculeData(
        id = "id2",
        actionKey = "actionKey",
        iconsRes = (R.drawable.ic_chip_shelter),
        title = UiText.DynamicString("Укриття")
    )

    val dataAll = MapChipMoleculeData(
        id = "id3",
        actionKey = "actionKey",
        iconsRes = (R.drawable.ic_chip_all),
        title = UiText.DynamicString("Всі"),
        selection = UIState.Selection.Selected
    )

    val btn = BtnWhiteAdditionalIconAtmData(
        icon = UiIcon.DrawableResource(DiiaResourceIcon.FILTER.code),
        badge = BadgeCounterAtmData(1),
        interactionState = UIState.Interaction.Enabled
    )
    MapChipTabsOrganism(data = MapChipTabsOrganismData(listOf(dataP, dataS, dataAll), btn)) {}
}
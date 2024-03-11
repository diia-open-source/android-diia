package ua.gov.diia.ui_base.components.molecule.header.chiptabbar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.ui_base.components.atom.divider.DividerSlimAtom
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.state.UIState
import ua.gov.diia.ui_base.components.theme.ColumbiaBlue

@Composable
fun ChipTabBarMolecule(
    modifier: Modifier = Modifier,
    data: ChipTabBarMoleculeData,
    onUIAction: (UIAction) -> Unit
) {
    val localData = remember { mutableStateOf(data) }
    val listState = rememberLazyListState()
    val performAutoScroll = remember { mutableStateOf(false) }
    val selectedItemId = remember { mutableStateOf("") }

    LaunchedEffect(key1 = data.tabs) {
        localData.value = data
        val selectedTab = data.tabs.firstOrNull { it.selectionState == UIState.Selection.Selected }
        if (selectedTab == null && localData.value.tabs.size != 0) {
            selectedItemId.value = localData.value.tabs[0].id
        }
        if (selectedTab != null && localData.value.tabs.size != 0 && localData.value.tabs[0].id != selectedTab.id) {
            selectedItemId.value = selectedTab.id
            performAutoScroll.value = true
        }
    }

    LaunchedEffect(key1 = selectedItemId.value) {
        if (selectedItemId.value != "") {
            localData.value = localData.value.copy(tabs = SnapshotStateList<ChipTabMoleculeData>().apply {
                localData.value.tabs.forEachIndexed { index, item ->
                    add(
                        item.copy(
                            selectionState = if (item.id == selectedItemId.value) {
                                UIState.Selection.Selected
                            } else {
                                UIState.Selection.Unselected
                            }
                        )
                    )
                }
            })
            if (selectedItemId.value != localData.value.tabs[0].id && performAutoScroll.value) {
                listState.scrollToItem(localData.value.tabs.indexOfFirst { item ->
                    item.id == selectedItemId.value
                })
                performAutoScroll.value = false
            }
        }
    }
    Column(modifier = modifier){
        LazyRow(
            modifier = Modifier.padding(vertical = 8.dp),
            state = listState
        ) {
            itemsIndexed(items = localData.value.tabs) { index, item ->
                if (index == 0) {
                    Spacer(modifier = Modifier.width(24.dp))
                }
                ChipTabMolecule(data = item, onUIAction = {
                    if (it.data != selectedItemId.value) {
                        selectedItemId.value = it.data ?: ""
                        onUIAction(it)
                    }
                })
                if (index != localData.value.tabs.size - 1) {
                    Spacer(modifier = Modifier.width(8.dp))
                }
                if (index == localData.value.tabs.size - 1) {
                    Spacer(modifier = Modifier.width(24.dp))
                }
            }
        }
        DividerSlimAtom(color = ColumbiaBlue)
    }

}

data class ChipTabBarMoleculeData(val tabs: SnapshotStateList<ChipTabMoleculeData>) : UIElementData


@Composable
@Preview
fun ChipTabBarMoleculePreview() {
    val data = ChipTabBarMoleculeData(tabs = SnapshotStateList<ChipTabMoleculeData>().apply {
        add(
            ChipTabMoleculeData(
                id = "1",
                title = "Label 1",
                counter = 0,
                selectionState = UIState.Selection.Selected
            )
        )
        add(
            ChipTabMoleculeData(
                id = "2",
                title = "Label 2",
                counter = 8,
                selectionState = UIState.Selection.Unselected
            )
        )
        add(
            ChipTabMoleculeData(
                id = "3",
                title = "Label 3",
                counter = 1,
                selectionState = UIState.Selection.Unselected
            )
        )
        add(
            ChipTabMoleculeData(
                id = "4",
                title = "Label 4",
                counter = 0,
                selectionState = UIState.Selection.Unselected
            )
        )
        add(
            ChipTabMoleculeData(
                id = "5",
                title = "Label 5",
                counter = 0,
                selectionState = UIState.Selection.Unselected
            )
        )
        add(
            ChipTabMoleculeData(
                id = "6",
                title = "Label 6",
                counter = 0,
                selectionState = UIState.Selection.Unselected
            )
        )
        add(
            ChipTabMoleculeData(
                id = "7",
                title = "Label 7",
                counter = 0,
                selectionState = UIState.Selection.Unselected
            )
        )
        add(
            ChipTabMoleculeData(
                id = "8",
                title = "Label 8",
                counter = 0,
                selectionState = UIState.Selection.Unselected
            )
        )
        add(
            ChipTabMoleculeData(
                id = "9",
                title = "Label 9",
                counter = 0,
                selectionState = UIState.Selection.Unselected
            )
        )
        add(
            ChipTabMoleculeData(
                id = "10",
                title = "Label 10",
                counter = 0,
                selectionState = UIState.Selection.Unselected
            )
        )
    })
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ColumbiaBlue)
    ) {
        ChipTabBarMolecule(data = data) {

        }
    }
}
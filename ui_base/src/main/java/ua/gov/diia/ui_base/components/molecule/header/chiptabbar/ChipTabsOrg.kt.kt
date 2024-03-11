package ua.gov.diia.ui_base.components.molecule.header.chiptabbar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.state.UIState
import ua.gov.diia.ui_base.components.theme.ColumbiaBlue

@Composable
fun ChipTabsOrg(
    modifier: Modifier = Modifier,
    data: ChipTabsOrgData,
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
            localData.value = localData.value.copy(tabs = SnapshotStateList<ChipTabMoleculeDataV2>().apply {
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

    LazyRow(
        modifier = modifier.padding(vertical = 16.dp).fillMaxWidth(),
        state = listState
    ) {
        itemsIndexed(items = localData.value.tabs) { index, item ->
            if (index == 0) {
                Spacer(modifier = Modifier.width(24.dp))
            }
            ChipTabMoleculeV2(data = item, onUIAction = {
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
}
data class ChipTabsOrgData(val tabs: SnapshotStateList<ChipTabMoleculeDataV2>) :
    UIElementData


@Composable
@Preview
fun ChipTabBarOrgPreview() {
    val data = ChipTabsOrgData(tabs = SnapshotStateList<ChipTabMoleculeDataV2>().apply {
        add(
            ChipTabMoleculeDataV2(
                id = "1",
                title = "Label 1",
                selectionState = UIState.Selection.Selected
            )
        )
        add(
            ChipTabMoleculeDataV2(
                id = "2",
                title = "Label 2",
                selectionState = UIState.Selection.Unselected
            )
        )

    })
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ColumbiaBlue)
    ) {
        ChipTabsOrg(data = data) {

        }
    }
}
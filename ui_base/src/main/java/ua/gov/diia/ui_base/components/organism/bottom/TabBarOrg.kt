package ua.gov.diia.ui_base.components.organism.bottom

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.ui_base.R
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.state.UIState
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.molecule.tab.TabItemMolecule
import ua.gov.diia.ui_base.components.molecule.tab.TabItemMoleculeData
import ua.gov.diia.ui_base.components.theme.Black

@Composable
fun TabBarOrg(
    modifier: Modifier = Modifier,
    data: TabBarOrganismData,
    lazyListState: LazyListState = rememberLazyListState(),
    onUIAction: (UIAction) -> Unit
) {
    LazyRow(
        modifier = modifier
            .background(Black)
            .fillMaxWidth()
            .padding(start = 24.dp, top = 16.dp, end = 24.dp, bottom = 12.dp)
            .testTag(data.componentId?.asString() ?: ""),

        userScrollEnabled = false,
        state = lazyListState,
        horizontalArrangement = Arrangement.SpaceBetween

    ) {
        itemsIndexed(items = data.tabs) { index, data ->
            TabItemMolecule(
                modifier = Modifier,
                data = data,
                onUIAction = onUIAction
            )
        }
    }
}

data class TabBarOrganismData(
    val tabs: SnapshotStateList<TabItemMoleculeData>,
    val componentId: UiText? = null
) : UIElementData {
    fun onTabClicked(tabId: String?): TabBarOrganismData {
        if (tabId == null) return this
        val data = this
        return this.copy(tabs = SnapshotStateList<TabItemMoleculeData>().apply {
            data.tabs.forEachIndexed { index, item ->
                add(
                    item.copy(
                        selectionState = if (item.id == tabId) {
                            UIState.Selection.Selected
                        } else {
                            UIState.Selection.Unselected
                        }
                    )
                )
            }
        })
    }
}

@Composable
@Preview
fun TapBarOrgPreview() {
    val data = TabBarOrganismData(tabs = SnapshotStateList<TabItemMoleculeData>().apply {
        add(
            TabItemMoleculeData(
                id = "1",
                label = "Label1",
                iconSelected = UiText.StringResource(R.drawable.ic_tab_documents_selected),
                iconUnselected = UiText.StringResource(R.drawable.ic_tab_documents_unselected),
                selectionState = UIState.Selection.Selected
            )
        )
        add(
            TabItemMoleculeData(
                id = "2",
                label = "Label2",
                iconSelected = UiText.StringResource(R.drawable.ic_tab_documents_selected),
                iconUnselected = UiText.StringResource(R.drawable.ic_tab_documents_unselected),
                selectionState = UIState.Selection.Unselected
            )
        )
        add(
            TabItemMoleculeData(
                id = "3",
                label = "Label3",
                iconSelected = UiText.StringResource(R.drawable.ic_tab_documents_selected),
                iconUnselected = UiText.StringResource(R.drawable.ic_tab_documents_unselected),
                selectionState = UIState.Selection.Unselected
            )
        )
        add(
            TabItemMoleculeData(
                id = "4",
                label = "Label4",
                iconSelected = UiText.StringResource(R.drawable.ic_tab_documents_selected),
                iconUnselected = UiText.StringResource(R.drawable.ic_tab_documents_unselected),
                selectionState = UIState.Selection.Unselected
            )
        )
    })

    val state = remember {
        mutableStateOf(data)
    }
    TabBarOrg(data = state.value) {
        state.value = state.value.onTabClicked(it.data)
    }
}

@Composable
@Preview
fun TapBarOrgPreviewWithBadge() {
    val data = TabBarOrganismData(tabs = SnapshotStateList<TabItemMoleculeData>().apply {
        add(
            TabItemMoleculeData(
                id = "1",
                label = "Label1",
                iconUnselected = UiText.StringResource(R.drawable.ic_tab_menu_unselected),
                iconSelected = UiText.StringResource(R.drawable.ic_tab_menu_selected),
                iconUnselectedWithBadge = UiText.StringResource(R.drawable.ic_tab_menu_unselected_badge),
                iconSelectedWithBadge = UiText.StringResource(R.drawable.ic_tab_menu_selected_badge),
                showBadge = true,
                selectionState = UIState.Selection.Selected

            )
        )
        add(
            TabItemMoleculeData(
                id = "2",
                label = "Label2",
                iconSelected = UiText.StringResource(R.drawable.ic_tab_documents_selected),
                iconUnselected = UiText.StringResource(R.drawable.ic_tab_documents_unselected),
                selectionState = UIState.Selection.Unselected
            )
        )
        add(
            TabItemMoleculeData(
                id = "3",
                label = "Label3",
                iconSelected = UiText.StringResource(R.drawable.ic_tab_documents_selected),
                iconUnselected = UiText.StringResource(R.drawable.ic_tab_documents_unselected),
                selectionState = UIState.Selection.Unselected
            )
        )
        add(
            TabItemMoleculeData(
                id = "4",
                label = "Label4",
                iconSelected = UiText.StringResource(R.drawable.ic_tab_documents_selected),
                iconUnselected = UiText.StringResource(R.drawable.ic_tab_documents_unselected),
                selectionState = UIState.Selection.Unselected
            )
        )
    })
    TabBarOrg(data = data) {}
}
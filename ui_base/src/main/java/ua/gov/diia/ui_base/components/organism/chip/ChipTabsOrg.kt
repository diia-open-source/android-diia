package ua.gov.diia.ui_base.components.organism.chip

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.core.models.common_compose.org.chip.ChipTabsOrg
import ua.gov.diia.ui_base.components.DiiaResourceIcon
import ua.gov.diia.ui_base.components.atom.divider.DividerSlimAtom
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.state.UIState
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiIcon
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.molecule.chip.ChipMlcData
import ua.gov.diia.ui_base.components.molecule.chip.toUIModel
import ua.gov.diia.ui_base.components.theme.PeriwinkleGray
import ua.gov.diia.ui_base.components.theme.PeriwinkleGrayAlpha30

@Composable
fun ChipTabsOrg(
    modifier: Modifier = Modifier,
    data: ChipTabsOrgData,
    onUIAction: (UIAction) -> Unit
) {
    val listState = rememberLazyListState()
    val performAutoScroll = remember { mutableStateOf(false) }

    LaunchedEffect(key1 = data.selectedItemId) {
        val selectedChip = data.chips.firstOrNull { it.id == data.selectedItemId }

        if (data.chips.size != 0 && data.chips[0].id != selectedChip?.id) {
            performAutoScroll.value = true
        }

        if (!data.selectedItemId.isNullOrEmpty()) {
            listState.scrollToItem(data.chips.indexOfFirst { item ->
                item.id == data.selectedItemId
            })
            performAutoScroll.value = false
        }
    }

    Column {
        LazyRow(
            modifier = modifier
                .padding(vertical = 16.dp)
                .fillMaxWidth()
                .testTag(data.componentId?.asString() ?: ""),
            state = listState
        ) {
            itemsIndexed(items = data.chips) { index, item ->
                if (index == 0) {
                    Spacer(modifier = Modifier.width(24.dp))
                }
                ua.gov.diia.ui_base.components.molecule.chip.ChipMlc(
                    data = item,
                    onUIAction = {
                        if (it.data != data.selectedItemId) {
                            onUIAction(it)
                        }
                    })
                if (index != data.chips.size - 1) {
                    Spacer(modifier = Modifier.width(8.dp))
                }
                if (index == data.chips.size - 1) {
                    Spacer(modifier = Modifier.width(24.dp))
                }
            }
        }
        DividerSlimAtom(
            modifier = Modifier.height(1.dp),
            color = PeriwinkleGray
        )
    }
}

data class ChipTabsOrgData(
    val chips: SnapshotStateList<ChipMlcData>,
    val componentId: UiText? = null,
    val selectedItemId: String? = null
) : UIElementData {

    fun onChipSelected(itemId: String): ChipTabsOrgData {
        return this.copy(
            chips = SnapshotStateList<ChipMlcData>().apply {
                chips.forEach { item ->
                    add(
                        item.copy(
                            selectionState = if (item.id == itemId) {
                                UIState.Selection.Selected
                            } else {
                                UIState.Selection.Unselected
                            }
                        )
                    )
                }
            },
            selectedItemId = itemId
        )
    }
}

fun ChipTabsOrg.toUiModel(): ChipTabsOrgData {
    val org = this
    return ChipTabsOrgData(
        chips = SnapshotStateList<ChipMlcData>().apply {
            items.forEach {
                it.chipMlc?.let { chipMlc ->
                    this.add(
                        chipMlc.toUIModel(
                            if (it.chipMlc?.code == org.preselectedCode) {
                                UIState.Selection.Selected
                            } else {
                                UIState.Selection.Unselected
                            }
                        )
                    )
                }
            }
        },
        selectedItemId = org.preselectedCode
    )
}

@Composable
@Preview
fun ChipTabsPSOrgPreview() {
    val data = ChipTabsOrgData(chips = SnapshotStateList<ChipMlcData>().apply {
        add(
            ChipMlcData(
                id = "1",
                label = UiText.DynamicString("label 1"),
                code = "inProgress",
                selectedIcon = UiIcon.DrawableResource(DiiaResourceIcon.ELLIPSE_CHECK.code),
                selectionState = UIState.Selection.Selected
            )
        )
        add(
            ChipMlcData(
                id = "2",
                label = UiText.DynamicString("label 2"),
                code = "inProgress",
                selectedIcon = UiIcon.DrawableResource(DiiaResourceIcon.ELLIPSE_CHECK.code),
                selectionState = UIState.Selection.Unselected
            )
        )

    })
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(PeriwinkleGrayAlpha30)
    ) {
        ChipTabsOrg(data = data) {

        }
    }
}
package ua.gov.diia.ui_base.components.infrastructure.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.loadItem
import ua.gov.diia.ui_base.components.molecule.header.chiptabbar.ChipTabsOrg
import ua.gov.diia.ui_base.components.molecule.header.chiptabbar.ChipTabsOrgData
import ua.gov.diia.ui_base.components.molecule.input.SearchInputV2
import ua.gov.diia.ui_base.components.molecule.input.SearchInputV2Data
import ua.gov.diia.ui_base.components.molecule.tile.ServiceCardTileOrgData
import ua.gov.diia.ui_base.components.molecule.tile.loadItems
import ua.gov.diia.ui_base.components.organism.list.MessageListOrganismData

//TODO migrate Feed and Menu tabs from TabBodyRootContainer to TabBodyRootLazyContainer
@Composable
fun ColumnScope.TabBodyRootLazyContainer(
    modifier: Modifier = Modifier,
    bodyViews: SnapshotStateList<UIElementData>,
    displayBlockDivider: Boolean = false,
    connectivityState: Boolean = true,
    progressIndicator: Pair<String, Boolean> = Pair("", false),
    contentLoaded: Pair<String, Boolean> = Pair("", false),
    onUIAction: (UIAction) -> Unit
) {
    val lazyListScrollState = rememberLazyListState()
    var displayBottomGradient by remember { mutableStateOf(false) }

    LaunchedEffect(
        key1 = lazyListScrollState.canScrollBackward,
        key2 = lazyListScrollState.canScrollForward
    ) {
        if (bodyViews.any { it is MessageListOrganismData }) {
            displayBottomGradient = displayBlockDivider && lazyListScrollState.canScrollForward
        }
    }

    Box(
        modifier = modifier.weight(1f),
        contentAlignment = Alignment.BottomCenter
    ) {
        LazyColumn(
            modifier = modifier
                .fillMaxSize(),
            contentPadding = PaddingValues(bottom = 16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            bodyViews.forEachIndexed { index, element ->
                when (element) {
                    is SearchInputV2Data -> {
                        loadItem(SearchInputV2Data::class) {
                            SearchInputV2(
                                modifier = Modifier,
                                data = element,
                                onUIAction = onUIAction
                            )
                        }
                    }

                    is ServiceCardTileOrgData -> {
                        loadItems(
                            item = element,
                            onUIAction = onUIAction
                        )
                    }

                    is ChipTabsOrgData -> {
                        loadItem(ChipTabsOrgData::class) {
                            ChipTabsOrg(
                                modifier = Modifier,
                                data = element,
                                onUIAction = onUIAction
                            )
                        }
                    }
                }
            }
        }
        NoInternetBlock(connectivityState)
        GradientDividerContentBlock(displayBottomGradient)
        LinearLoaderContentBlock(contentLoaded)
    }
}
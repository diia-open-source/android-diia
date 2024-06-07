package ua.gov.diia.ui_base.components.organism.list

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import kotlinx.coroutines.flow.Flow
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.molecule.card.CardMlc
import ua.gov.diia.ui_base.components.molecule.card.CardMlcData


@Deprecated("Use SimplePaginationListOrganism")
@Composable
fun PaginatedCardListOrg(
    modifier: Modifier = Modifier,
    data: PaginatedCardListOrgData,
    lazyListState: LazyListState = rememberLazyListState(),
    progressIndicator: Pair<String, Boolean> = Pair("", false),
    onUIAction: (UIAction) -> Unit
) {
    val items = data.items.collectAsLazyPagingItems()

    LazyColumn(
        modifier = modifier,
        state = lazyListState,
        horizontalAlignment = Alignment.CenterHorizontally,
        contentPadding = PaddingValues(
            bottom = 16.dp,
        ),
    ) {
        items(
            count = items.itemCount,
            key = items.itemKey(key = { it.id }
            ),
            contentType = items.itemContentType()
        ) { index ->
            val item = items[index]
            item?.let {
                CardMlc(
                    data = item,
                    progressIndicator = progressIndicator,
                    onUIAction = onUIAction
                )
            }
        }
    }
}

@Deprecated("Use SimplePaginationListOrganism")
data class PaginatedCardListOrgData(val items: Flow<PagingData<CardMlcData>>) : UIElementData

fun LazyListScope.loadPaginatedCardListOrg(
    items: LazyPagingItems<CardMlcData>,
    progressIndicator: Pair<String, Boolean> = Pair("", false),
    onUIAction: (UIAction) -> Unit = {}
) {
    items(
        count = items.itemCount,
        key = items.itemKey(key = { it.id }),
        contentType = items.itemContentType()
    ) { index ->
        val item = items[index]
        item?.let {
            CardMlc(
                data = item,
                progressIndicator = progressIndicator,
                onUIAction = onUIAction
            )
        }
    }
    item {
        Spacer(Modifier.height(16.dp))
    }
}

@Composable
@Preview
fun PaginatedCardListOrganismPreview() {
    //TBA
}

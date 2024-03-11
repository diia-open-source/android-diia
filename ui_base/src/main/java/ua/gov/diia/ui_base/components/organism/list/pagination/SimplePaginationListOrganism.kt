package ua.gov.diia.ui_base.components.organism.list.pagination

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import kotlinx.coroutines.flow.Flow
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.molecule.card.HalvedCardMlc
import ua.gov.diia.ui_base.components.molecule.card.HalvedCardMlcData
import ua.gov.diia.ui_base.components.molecule.card.ProcessCardMoleculeDeprecated
import ua.gov.diia.ui_base.components.molecule.card.ProcessCardMoleculeDataDeprecated

/**
 * Internal organism, not implemented at Design System
 * Use it to display simple pagination list
 * Use [SimplePaginationCard] for all types of cards
 */
@Composable
fun SimplePaginationListOrganism(
    modifier: Modifier = Modifier,
    data: SimplePaginationListOrganismData,
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
            start = 24.dp,
            end = 24.dp,
            bottom = 24.dp
        ),
    ) {
        items(
            count = items.itemCount,
            key = items.itemKey(key = { it.id }),
            contentType = items.itemContentType()
        ) { index ->
            val item = items[index]
            item?.let {
                Spacer(modifier = Modifier.size(16.dp))
                when (it) {
                    is HalvedCardMlcData -> {
                        HalvedCardMlc(
                            data = it,
                            onUIAction = onUIAction
                        )
                    }

                    is ProcessCardMoleculeDataDeprecated -> {
                        ProcessCardMoleculeDeprecated(
                            data = it,
                            progressIndicator = progressIndicator,
                            onUIAction = onUIAction
                        )
                    }
                }
            }
        }
    }
}

data class SimplePaginationListOrganismData(val items: Flow<PagingData<SimplePaginationCard>>) :
    UIElementData


interface SimplePaginationCard : UIElementData {
    val id: String
}
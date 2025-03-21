package ua.gov.diia.ui_base.components.organism.list.pagination

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import kotlinx.coroutines.flow.Flow
import ua.gov.diia.ui_base.components.atom.button.ButtonStrokeAdditionalAtomData
import ua.gov.diia.ui_base.components.atom.media.ArticlePicAtm
import ua.gov.diia.ui_base.components.atom.media.ArticlePicAtmData
import ua.gov.diia.ui_base.components.atom.text.GreyTitleAtm
import ua.gov.diia.ui_base.components.atom.text.GreyTitleAtmData
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.state.UIState
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.molecule.card.CardMlc
import ua.gov.diia.ui_base.components.molecule.card.CardMlcData
import ua.gov.diia.ui_base.components.molecule.card.CardMlcV2
import ua.gov.diia.ui_base.components.molecule.card.CardMlcV2Data
import ua.gov.diia.ui_base.components.molecule.card.HalvedCardMlc
import ua.gov.diia.ui_base.components.molecule.card.HalvedCardMlcData
import ua.gov.diia.ui_base.components.molecule.card.ImageCardMlc
import ua.gov.diia.ui_base.components.molecule.card.ImageCardMlcData
import ua.gov.diia.ui_base.components.molecule.list.ListItemMlc
import ua.gov.diia.ui_base.components.molecule.list.ListItemMlcData
import ua.gov.diia.ui_base.components.molecule.list.ListWidgetItemMlc
import ua.gov.diia.ui_base.components.molecule.list.ListWidgetItemMlcData
import ua.gov.diia.ui_base.components.molecule.message.StubMessageMlc
import ua.gov.diia.ui_base.components.molecule.message.StubMessageMlcData
import ua.gov.diia.ui_base.components.subatomic.loader.LoaderSpinnerLoaderAtm
import ua.gov.diia.ui_base.components.theme.PeriwinkleGray

/**
 * Internal organism, not implemented at Design System
 * Use it to display simple pagination list
 * Use [SimplePagination] for all types of cards
 */
@Composable
fun PaginationListOrg(
    modifier: Modifier = Modifier,
    data: PaginationListOrgData,
    lazyListState: LazyListState = rememberLazyListState(),
    onUIAction: (UIAction) -> Unit
) {

    val items = data.items.collectAsLazyPagingItems()
    LazyColumn(
        modifier = modifier,
        state = lazyListState,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        loadPaginationListOrg(modifier, items, onUIAction)
    }
}

@Composable
fun ErrorMsgMlc(doAction: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            modifier = Modifier.padding(top = 20.dp),
            text = "Не вдалося завантажити дані, спробуйте ще раз",
            textAlign = TextAlign.Center,
        )
        ua.gov.diia.ui_base.components.atom.button.BtnStrokeAdditionalAtm(
            modifier = Modifier.padding(top = 16.dp),
            data = ButtonStrokeAdditionalAtomData(
                actionKey = UIActionKeysCompose.BUTTON_REGULAR,
                id = "stub_message_mlc_refresh",
                title = UiText.DynamicString("Оновити"),
                interactionState = UIState.Interaction.Enabled,
            ),
            onUIAction = { doAction() }
        )
        Spacer(modifier = Modifier.height(32.dp))
    }
}

data class PaginationListOrgData(
    val items: Flow<PagingData<SimplePagination>>
) : UIElementData

fun LazyListScope.loadPaginationListOrg(
    modifier: Modifier = Modifier,
    items: LazyPagingItems<SimplePagination>,
    onUIAction: (UIAction) -> Unit = {}
) {
    items(
        count = items.itemCount,
        key = items.itemKey(key = { it.id }),
        contentType = items.itemContentType()
    ) { index ->
        val item = items[index]
        item?.let {
            when (it) {
                is ArticlePicAtmData -> {
                    ArticlePicAtm(
                        modifier = modifier.padding(horizontal = 24.dp).padding(top = 24.dp),
                        data = it,
                        inCarousel = true,
                        onUIAction = onUIAction
                    )
                }
                is CardMlcData -> {
                    CardMlc(
                        data = it,
                        onUIAction = onUIAction
                    )
                }
                is CardMlcV2Data -> {
                    CardMlcV2(
                        data = it,
                        onUIAction = onUIAction
                    )
                }
                is ImageCardMlcData -> {
                    ImageCardMlc(
                        data = it,
                        onUIAction = onUIAction
                    )
                }
                is ListWidgetItemMlcData -> {
                    ListWidgetItemMlc(
                        data = it,
                        onUIAction = onUIAction
                    )
                }
                is GreyTitleAtmData -> {
                    GreyTitleAtm(
                        modifier = Modifier.padding(horizontal = 8.dp),
                        data = it,
                    )
                }
                is ListItemMlcData -> {
                    ListItemMlc(
                        modifier = Modifier.padding(horizontal = 8.dp),
                        data = it,
                        onUIAction = onUIAction
                    )
                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = 24.dp),
                        thickness = 1.dp,
                        color = PeriwinkleGray
                    )
                }
                is HalvedCardMlcData -> {
                    HalvedCardMlc(
                        modifier = modifier.padding(horizontal = 24.dp).padding(top = if(index > 0)16.dp else 0.dp),
                        data = it,
                        onUIAction = onUIAction
                    )
                }
                is StubMessageMlcData -> {
                    StubMessageMlc(
                        data = it,
                        onUIAction = onUIAction
                    )
                }
            }
        }
    }

    when (items.loadState.refresh) {
        is LoadState.Loading -> {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 24.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    LoaderSpinnerLoaderAtm()
                }
            }
        }

        is LoadState.Error -> {
            item {
                ErrorMsgMlc { items.retry() }
            }
        }

        is LoadState.NotLoading -> {
            Unit
        }

    }

    when (items.loadState.append) {
        is LoadState.NotLoading -> {
            Unit
        }

        is LoadState.Loading -> {
            item {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center,
                ) {
                    LoaderSpinnerLoaderAtm()
                }
            }
        }

        is LoadState.Error -> {
            item {
                ErrorMsgMlc { items.retry() }
            }
        }
    }
}


interface SimplePagination : UIElementData {
    val id: String
}
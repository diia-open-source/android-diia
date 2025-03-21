package ua.gov.diia.ui_base.components.organism.list.pagination

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import kotlinx.coroutines.flow.Flow
import ua.gov.diia.ui_base.components.atom.button.BtnStrokeAdditionalAtm
import ua.gov.diia.ui_base.components.atom.button.ButtonStrokeAdditionalAtomData
import ua.gov.diia.ui_base.components.atom.divider.DividerSlimAtom
import ua.gov.diia.ui_base.components.atom.media.ArticlePicAtm
import ua.gov.diia.ui_base.components.atom.media.ArticlePicAtmData
import ua.gov.diia.ui_base.components.atom.text.GreyTitleAtm
import ua.gov.diia.ui_base.components.atom.text.GreyTitleAtmData
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.state.UIState
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.molecule.card.CardMlcV2
import ua.gov.diia.ui_base.components.molecule.card.CardMlcV2Data
import ua.gov.diia.ui_base.components.molecule.list.ListItemMlc
import ua.gov.diia.ui_base.components.molecule.list.ListItemMlcData
import ua.gov.diia.ui_base.components.molecule.message.PaginationMessageMlc
import ua.gov.diia.ui_base.components.molecule.message.generatePaginationMessageMlcMockData
import ua.gov.diia.ui_base.components.subatomic.loader.LoaderSpinnerLoaderAtm
import ua.gov.diia.ui_base.components.theme.BlackSqueeze

@Composable
fun PaginationListWhiteOrg(
    modifier: Modifier = Modifier,
    data: PaginationListWhiteOrgData,
    lazyListState: LazyListState = rememberLazyListState(),
    withDividers: Boolean? = null,
    onUIAction: (UIAction) -> Unit
) {
    val items = data.items.collectAsLazyPagingItems()
    LazyColumn(
        modifier = modifier,
        state = lazyListState,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        loadPaginationListWhiteOrg(modifier, items, lazyListState, withDividers, onUIAction)
    }
}

data class PaginationListWhiteOrgData(
    val items: Flow<PagingData<SimplePagination>>,
    val withDividers: Boolean? = null
) : UIElementData

fun LazyListScope.loadPaginationListWhiteOrg(
    modifier: Modifier = Modifier,
    items: LazyPagingItems<SimplePagination>,
    state: LazyListState,
    withDividers: Boolean? = null,
    onUIAction: (UIAction) -> Unit = {}
) {

    val isDataLoaded = items.loadState.refresh is LoadState.NotLoading && items.itemCount > 0

    if (isDataLoaded) {
        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
    items(
        count = items.itemCount,
        key = items.itemKey(key = { it.id }),
        contentType = items.itemContentType()
    ) { index ->
        val item = items[index]
        item?.let {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .background(
                        color = Color.White,
                        shape = when (index) {
                            0 -> {
                                if (items.itemCount == 1) {
                                    RoundedCornerShape(16.dp)
                                } else {
                                    RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
                                }
                            }

                            items.itemCount - 1 -> {
                                if (items.loadState.append is LoadState.Loading ||
                                    items.loadState.append is LoadState.Error
                                ) {
                                    RectangleShape
                                } else {
                                    RoundedCornerShape(
                                        bottomStart = 16.dp,
                                        bottomEnd = 16.dp
                                    )
                                }
                            }

                            else -> {
                                RectangleShape
                            }
                        }
                    )
            ) {
                when (it) {
                    is GreyTitleAtmData -> {
                        GreyTitleAtm(
                            data = it,
                        )
                    }

                    is ArticlePicAtmData -> {
                        ArticlePicAtm(
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

                    is ListItemMlcData -> {
                        ListItemMlc(
                            data = it,
                            onUIAction = onUIAction
                        )
                        if (withDividers != null && withDividers == true) {
                            if (index > 0) {
                                DividerSlimAtom(
                                    modifier = Modifier.fillMaxWidth(),
                                    color = BlackSqueeze
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    when (items.loadState.refresh) {
        is LoadState.Loading -> {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center,
                ) {
                    LoaderSpinnerLoaderAtm()
                }
            }
        }

        is LoadState.Error -> {
            item {
                PaginationMessageMlc(
                    data = generatePaginationMessageMlcMockData(),
                    onUIAction = {
                        items.retry()
                    }
                )
            }
        }

        else -> Unit
    }

    when (items.loadState.append) {
        is LoadState.Loading -> {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                        .background(
                            color = Color.White,
                            shape = RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp)
                        ),
                    contentAlignment = Alignment.Center,
                ) {
                    LoaderSpinnerLoaderAtm()
                }
            }
        }

        is LoadState.Error -> {
            item {
                LaunchedEffect(Unit) {
                    state.animateScrollToItem(items.itemCount)

                }
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                        .background(
                            color = Color.White,
                            shape = RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp)
                        ),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        modifier = Modifier.padding(top = 20.dp, start = 24.dp, end = 24.dp),
                        text = "Не вдалося завантажити дані, спробуйте ще раз",
                        textAlign = TextAlign.Center,
                    )
                    BtnStrokeAdditionalAtm(
                        modifier = Modifier.padding(top = 16.dp),
                        data = ButtonStrokeAdditionalAtomData(
                            actionKey = UIActionKeysCompose.BUTTON_REGULAR,
                            id = "stub_message_mlc_refresh",
                            title = UiText.DynamicString("Оновити"),
                            interactionState = UIState.Interaction.Enabled,
                        ),
                        onUIAction = {
                            items.retry()
                        }
                    )
                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
        }

        else -> Unit
    }
}
package ua.gov.diia.ui_base.components.organism.carousel

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import ua.gov.diia.ui_base.components.DiiaResourceIconProvider
import ua.gov.diia.ui_base.components.atom.media.ArticlePicAtm
import ua.gov.diia.ui_base.components.atom.media.ArticlePicAtmData
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.molecule.card.HalvedCardMlc
import ua.gov.diia.ui_base.components.molecule.card.HalvedCardMlcData
import ua.gov.diia.ui_base.components.molecule.card.IconCardMlc
import ua.gov.diia.ui_base.components.molecule.card.IconCardMlcData
import ua.gov.diia.ui_base.components.molecule.card.SmallNotificationMlc
import ua.gov.diia.ui_base.components.molecule.card.SmallNotificationMlcData
import ua.gov.diia.ui_base.components.molecule.media.ArticleVideoMlc
import ua.gov.diia.ui_base.components.molecule.media.ArticleVideoMlcData
import ua.gov.diia.ui_base.components.atom.pager.DotNavigationAtm
import ua.gov.diia.ui_base.components.atom.pager.DotNavigationAtmData


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BaseSimpleCarouselInternal(
    modifier: Modifier = Modifier,
    data: BaseSimpleCarouselInternalData,
    connectivityState: Boolean = true,
    diiaResourceIconProvider: DiiaResourceIconProvider,
    onUIAction: (UIAction) -> Unit
) {
    val pagerState = rememberPagerState(
        initialPage = 0,
        initialPageOffsetFraction = 0f
    ) {
       data.items.size
    }
    var position by remember { mutableStateOf(0) }
    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }
            .collect { currentPage ->
                position = currentPage
            }
    }

    val localDensity = LocalDensity.current

    var cardHeight by remember {
        mutableStateOf(0.dp)
    }

    Column(
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            HorizontalPager(
                modifier = Modifier,
                state = pagerState,
                contentPadding = PaddingValues(
                    horizontal = 24.dp,
                ),
                pageSize = PageSize.Fill,
                pageSpacing = 10.dp,
                pageContent = { pageIndex ->
                    when (val card = data.items[pageIndex]) {
                        is HalvedCardMlcData -> {
                            HalvedCardMlc(
                                modifier = Modifier.onGloballyPositioned { coordinates ->
                                    cardHeight =
                                        with(localDensity) { coordinates.size.height.toDp() }
                                },
                                data = card,
                                clickable = pageIndex == position,
                                onUIAction = onUIAction
                            )
                        }

                        is SmallNotificationMlcData -> {
                            SmallNotificationMlc(
                                modifier = Modifier.onGloballyPositioned { coordinates ->
                                    cardHeight =
                                        with(localDensity) { coordinates.size.height.toDp() }
                                },
                                data = card,
                                clickable = pageIndex == position,
                                onUIAction = onUIAction
                            )
                        }

                        is ArticlePicAtmData -> {
                            ArticlePicAtm(
                                modifier = Modifier,
                                data = card,
                                inCarousel = true,
                                clickable = pageIndex == position,
                                onUIAction = onUIAction
                            )
                        }

                        is ArticleVideoMlcData -> {
                            ArticleVideoMlc(
                                modifier = Modifier,
                                data = card,
                                inCarousel = true,
                                clickable = pageIndex == position,
                                connectivityState = connectivityState
                            )
                        }

                        is IconCardMlcData -> {
                            IconCardMlc(
                                modifier = Modifier.height(cardHeight),
                                data = card,
                                clickable = pageIndex == position,
                                diiaResourceIconProvider = diiaResourceIconProvider,
                                onUIAction = onUIAction
                            )
                        }
                    }
                }
            )
        }

        if (data.items.size > 1) {
            DotNavigationAtm(
                modifier = Modifier
                    .padding(top = 16.dp)
                    .align(Alignment.CenterHorizontally),
                data = DotNavigationAtmData(
                    activeDotIndex = position,
                    totalCount = data.items.size
                )
            )
        }
    }
}

interface BaseSimpleCarouselInternalData : UIElementData {
    val items: List<SimpleCarouselCard>
}

interface SimpleCarouselCard : UIElementData

package ua.gov.diia.ui_base.components.organism.pager

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalOverscrollConfiguration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.calculateCurrentOffsetForPage
import com.google.accompanist.pager.rememberPagerState
import ua.gov.diia.ui_base.components.atom.pager.DocDotNavigationAtm
import ua.gov.diia.ui_base.components.atom.pager.DocDotNavigationAtmData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.organism.document.AddDocOrg
import ua.gov.diia.ui_base.components.organism.document.AddDocOrgData
import ua.gov.diia.ui_base.components.organism.document.DocOrg
import ua.gov.diia.ui_base.components.organism.document.DocOrgData
import java.util.UUID
import kotlin.math.absoluteValue

@OptIn(ExperimentalPagerApi::class, ExperimentalFoundationApi::class)
@Composable
fun BaseCarouselOrg(
    modifier: Modifier = Modifier,
    data: BaseCarouselOrgData,
    onUIAction: (UIAction) -> Unit,
    progressIndicator: Pair<String, Boolean> = Pair("", false),
    pageCount: Int
) {
    val configuration = LocalConfiguration.current
    val screenWidthDp = configuration.screenWidthDp
    val screenHeightDp = configuration.screenHeightDp

    val screenDensity = LocalDensity.current.density

    val screenWidthPx = (screenWidthDp * screenDensity).toInt()
    val screenHeightPx = (screenHeightDp * screenDensity).toInt()

    val screenDiagonalInches = kotlin.math.sqrt(
        (screenWidthPx * screenWidthPx + screenHeightPx * screenHeightPx).toDouble()
    ) / (160 * screenDensity)

    var fraction by remember {
        mutableFloatStateOf(
            when {
                screenDiagonalInches < 5.10 -> 0.9f
                screenDiagonalInches > 5.10 && screenDiagonalInches < 5.25 -> 0.75f
                else -> 0.7f
            }
        )
    }

    val screenHeightInPixels = with(LocalDensity.current) { screenHeightDp.dp.toPx() }
    val desiredHeightInPixels = screenHeightInPixels * fraction
    val actualFraction = desiredHeightInPixels / screenHeightInPixels


    val state = rememberPagerState()
    var scrollInProgress = remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        snapshotFlow {
            state.isScrollInProgress
        }.collect {
            scrollInProgress.value = it
        }
    }

    LaunchedEffect(state) {
        snapshotFlow { state.currentPage }.collect { page ->
            onUIAction.invoke(
                UIAction(actionKey = UIActionKeysCompose.DOC_PAGE_SELECTED, data = page.toString())
            )
        }
    }

    LaunchedEffect(key1 = state.currentPage) {
        onUIAction(UIAction(actionKey = UIActionKeysCompose.DOC_CARD_SWIPE_FINISHED))
    }

    Column(
        Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {
        CompositionLocalProvider(
            LocalOverscrollConfiguration provides null
        ) {
            HorizontalPager(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(actualFraction),
                count = pageCount,
                state = state,
                itemSpacing = (16).dp,
                key = { "${UUID.randomUUID()}" },
                contentPadding = PaddingValues(
                    start = 32.dp, end = 32.dp
                )
            ) { page ->
                val card = data.card[page]
                val docModifier = Modifier
                    .graphicsLayer {
                        val pageOffset = calculateCurrentOffsetForPage(page).absoluteValue
                        lerp(
                            start = 0.85f,
                            stop = 1f,
                            fraction = 1f - pageOffset.coerceIn(0f, 1f)
                        ).also { scale ->
                            scaleX = 1f
                            scaleY = scale
                        }
                    }
                when (card) {
                    is DocCardFlipData -> {
                        DocCardFlip(
                            modifier = docModifier,
                            data = card,
                            progressIndicator = progressIndicator,
                            cardFocus = if (scrollInProgress.value) {
                                CardFocus.UNDEFINED
                            } else {
                                if (state.currentPage == page) {
                                    CardFocus.IN_FOCUS
                                } else {
                                    CardFocus.OUT_OF_FOCUS
                                }

                            }
                        ) {
                            if (state.currentPage == page) {
                                onUIAction(
                                    UIAction(
                                        data = it.data,
                                        optionalId = page.toString(),
                                        actionKey = it.actionKey
                                    )
                                )
                            }
                        }
                    }

                    is DocOrgData -> {
                        DocOrg(
                            modifier = docModifier,
                            data = card,
                            cardFocus = if (scrollInProgress.value) {
                                CardFocus.UNDEFINED
                            } else {
                                if (state.currentPage == page) {
                                    CardFocus.IN_FOCUS
                                } else {
                                    CardFocus.OUT_OF_FOCUS
                                }

                            }
                        ) {
                            onUIAction(
                                UIAction(
                                    data = it.data,
                                    optionalId = page.toString(),
                                    actionKey = it.actionKey
                                )
                            )
                        }
                    }

                    is AddDocOrgData -> {
                        AddDocOrg(
                            modifier = docModifier,
                            data = card,
                            onUIAction = onUIAction
                        )
                    }
                }
            }
        }
        DocDotNavigationAtm(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .alpha(if (state.pageCount > 1) 1f else 0f),
            data = DocDotNavigationAtmData(
                activeDotIndex = state.currentPage,
                totalCount = pageCount
            )
        )
    }

    if (state.pageCount > 0) {
        LaunchedEffect(key1 = data.focusOnDoc, block = {
            if (data.focusOnDoc != null && data.focusOnDoc != 0) {
                if (data.focusOnDoc >= state.pageCount) {
                    //scroll to last
                    state.scrollToPage(state.pageCount - 1)
                } else {
                    state.scrollToPage(data.focusOnDoc)
                }
            }
        })
    }
}

data class BaseCarouselOrgData(val card: List<DocsCarouselItem>, val focusOnDoc: Int? = null)

enum class CardFocus {
    OUT_OF_FOCUS, IN_FOCUS, UNDEFINED
}
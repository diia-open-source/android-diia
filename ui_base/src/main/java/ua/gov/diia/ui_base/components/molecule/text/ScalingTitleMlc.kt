package ua.gov.diia.ui_base.components.molecule.text

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import ua.gov.diia.core.models.common_compose.mlc.text.ScalingTitleMlc
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.theme.Black
import ua.gov.diia.ui_base.components.theme.DiiaTextStyle

@Composable
fun ScalingTitleMlc(
    modifier: Modifier = Modifier,
    data: ScalingTitleMlcData,
    lazyListState: LazyListState,
    alphaCallback: (Float) -> Unit = {}
) {
    val density = LocalDensity.current

    val minThresholdPx = with(density) { 16.dp.toPx() }

    val maxThresholdPx = with(density) { 48.dp.toPx() }

    val isFirstItemVisible by remember { derivedStateOf { lazyListState.firstVisibleItemIndex == 0 } }

    val scrollOffset by remember { derivedStateOf { lazyListState.firstVisibleItemScrollOffset.toFloat() } }

    val isFirstItemFullyVisible by remember {
        derivedStateOf {
            lazyListState.firstVisibleItemIndex == 0 &&
                lazyListState.firstVisibleItemScrollOffset == 0
        }
    }

    val isLastItemFullyVisible by remember {
        derivedStateOf {
            val layoutInfo = lazyListState.layoutInfo

            val lastVisibleItem =
                layoutInfo.visibleItemsInfo.lastOrNull() ?: return@derivedStateOf false

            if (lastVisibleItem.index != layoutInfo.totalItemsCount - 1) {
                false
            } else {
                lastVisibleItem.offset >= layoutInfo.viewportStartOffset &&
                    (lastVisibleItem.offset + lastVisibleItem.size) <= layoutInfo.viewportEndOffset
            }
        }
    }

    val targetAlpha = when {
        isFirstItemFullyVisible -> 0F

        isLastItemFullyVisible -> 1F

        isFirstItemVisible -> when {
            scrollOffset < minThresholdPx -> 0F

            scrollOffset in minThresholdPx..maxThresholdPx -> {
                (scrollOffset - minThresholdPx) / (maxThresholdPx - minThresholdPx)
            }

            else -> 1F
        }

        else -> 1F
    }

    val animatedAlpha by animateFloatAsState(
        targetValue = targetAlpha,
        animationSpec = tween(durationMillis = 300)
    )

    alphaCallback(animatedAlpha)

    Box(
        modifier = modifier
            .padding(start = 24.dp)
            .testTag(data.componentId.orEmpty()),
        contentAlignment = Alignment.Center
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .alpha(1F - animatedAlpha),
            text = data.label,
            style = DiiaTextStyle.h2MediumHeading,
            color = Black,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .alpha(animatedAlpha),
            text = data.label,
            style = DiiaTextStyle.h3SmallHeading,
            color = Black,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}

data class ScalingTitleMlcData(
    val componentId: String? = null,
    val label: String
) : UIElementData

fun ScalingTitleMlc.toUiModel() = ScalingTitleMlcData(
    componentId = componentId,
    label = label
)
package ua.gov.diia.ui_base.components.atom.pager

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import ua.gov.diia.ui_base.components.theme.Black
import ua.gov.diia.ui_base.components.theme.BlackAlpha40
import ua.gov.diia.ui_base.components.theme.Transparent

private const val MAXIMUM_CONTAINER_SIZE_MULTIPLIER = 6

@Composable
fun BaseViewPagerIndicator(
    modifier: Modifier = Modifier,
    activeDotIndex: Int = 0,
    totalDotsCounter: Int,
    activeDotColor: Color = Black,
    inactiveDotColor: Color = BlackAlpha40,
) {
    val lazyListState: LazyListState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val dotElementContainerSize = 16.dp
    val bigDotSize = 8.dp
    val mediumDotSize = 6.dp
    val smallDotSize = 4.dp

    val items = mutableListOf<Int>().apply {
        repeat(totalDotsCounter) {
            add(it)
        }
    }

    LaunchedEffect(key1 = activeDotIndex) {
        coroutineScope.launch {
            if (activeDotIndex > 0) {
                lazyListState.animateScrollToItem(index = activeDotIndex - 1)
            }
        }
    }

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        LazyRow(
            modifier = Modifier.width(
                if (totalDotsCounter > MAXIMUM_CONTAINER_SIZE_MULTIPLIER - 1) {
                    dotElementContainerSize * MAXIMUM_CONTAINER_SIZE_MULTIPLIER
                } else {
                    dotElementContainerSize * totalDotsCounter
                }
            ),
            userScrollEnabled = false,
            state = lazyListState,
        ) {
            itemsIndexed(items) { index, item ->
                BaseViewPagerDot(
                    dotSizeType = getDotSizeType(index, activeDotIndex, totalDotsCounter),
                    isActive = index == activeDotIndex,
                    smallSize = smallDotSize,
                    mediumSize = mediumDotSize,
                    bigSize = bigDotSize,
                    containerSize = dotElementContainerSize,
                    activeDotColor = activeDotColor,
                    inactiveDotColor = inactiveDotColor
                )
            }
        }
    }
}

fun getDotSizeType(
    index: Int,
    activeDot: Int,
    totalDotsCounter: Int
): BaseViewPagerDotSizeType {
    if (activeDot == 0) {
        return when (index) {
            0, 1, 2 -> {
                BaseViewPagerDotSizeType.BIG
            }
            3, 4 -> {
                BaseViewPagerDotSizeType.MEDIUM
            }
            else -> {
                BaseViewPagerDotSizeType.SMALL
            }
        }
    }
    if (activeDot == totalDotsCounter - 1) {
        return when (index) {
            totalDotsCounter - 1, totalDotsCounter - 2, totalDotsCounter - 3 -> {
                BaseViewPagerDotSizeType.BIG
            }
            totalDotsCounter - 4, totalDotsCounter - 5 -> {
                BaseViewPagerDotSizeType.MEDIUM
            }
            else -> {
                BaseViewPagerDotSizeType.SMALL
            }
        }
    }
    return when {
        index == activeDot || index == activeDot - 1 || index == activeDot + 1 -> BaseViewPagerDotSizeType.BIG
        activeDot - index == -5 || activeDot - index == 5 -> BaseViewPagerDotSizeType.SMALL
        else -> BaseViewPagerDotSizeType.MEDIUM
    }
}

@Composable
fun BaseViewPagerDot(
    isActive: Boolean,
    dotSizeType: BaseViewPagerDotSizeType,
    containerSize: Dp,
    bigSize: Dp,
    mediumSize: Dp,
    smallSize: Dp,
    activeDotColor: Color,
    inactiveDotColor: Color
) {
    Box(
        modifier = Modifier
            .size(containerSize)
            .background(Transparent),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .clip(shape = RoundedCornerShape(16.dp))
                .background(if (isActive) activeDotColor else inactiveDotColor)
                .animateContentSize(
                    animationSpec = tween(
                        durationMillis = 500,
                        easing = LinearOutSlowInEasing
                    )
                )
                .size(
                    size = when (dotSizeType) {
                        BaseViewPagerDotSizeType.BIG -> bigSize
                        BaseViewPagerDotSizeType.MEDIUM -> mediumSize
                        BaseViewPagerDotSizeType.SMALL -> smallSize
                    }
                ), content = {})
    }
}


@Composable
private fun BaseViewPagerIndicatorGeneralPreview(totalDotsCounter: Int) {
    val position = remember { mutableStateOf(0) }
    Column(
        modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally
    ) {
        BaseViewPagerIndicator(
            activeDotIndex = position.value,
            totalDotsCounter = totalDotsCounter
        )
        Spacer(modifier = Modifier.height(20.dp))
        Text("Active index = ${position.value}")
        Spacer(modifier = Modifier.height(20.dp))
        Row() {
            Button(enabled = position.value > 0, onClick = {
                if (position.value > 0) {
                    position.value = position.value - 1
                }
            }) {
                Text("-")
            }
            Spacer(modifier = Modifier.width(50.dp))
            Button(enabled = position.value < totalDotsCounter - 1, onClick = {
                if (position.value < totalDotsCounter) {
                    position.value = position.value + 1
                }
            }) {
                Text("+")
            }
        }

    }
}

@Preview
@Composable
fun BaseViewPagerIndicatorPreview_TwoElement() {
    BaseViewPagerIndicatorGeneralPreview(totalDotsCounter = 2)
}

@Preview
@Composable
fun BaseViewPagerIndicatorPreview_ThreeElement() {
    BaseViewPagerIndicatorGeneralPreview(totalDotsCounter = 3)
}

@Preview
@Composable
fun BaseViewPagerIndicatorPreview_FourElement() {
    BaseViewPagerIndicatorGeneralPreview(totalDotsCounter = 4)
}

@Preview
@Composable
fun BaseViewPagerIndicatorPreview_FiveElement() {
    BaseViewPagerIndicatorGeneralPreview(totalDotsCounter = 5)
}

@Preview
@Composable
fun BaseViewPagerIndicatorPreview_TenElement() {
    BaseViewPagerIndicatorGeneralPreview(totalDotsCounter = 10)
}

enum class BaseViewPagerDotSizeType {
    BIG, MEDIUM, SMALL
}
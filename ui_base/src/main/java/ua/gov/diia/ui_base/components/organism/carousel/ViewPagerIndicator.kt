package ua.gov.diia.ui_base.components.organism.carousel

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import ua.gov.diia.ui_base.components.theme.Transparent
import ua.gov.diia.ui_base.components.theme.White


private const val MAXIMUM_CONTAINER_SIZE_MULTIPLIER = 5
private const val SMALL_GROUP_MAX_ELEMENT_COUNT = 4

@Composable
fun ViewPagerIndicator(
    modifier: Modifier = Modifier,
    type: ViewPagerIndicatorSizeType = ViewPagerIndicatorSizeType.BIG,
    activeDotIndex: Int = 0,
    totalDotsCounter: Int,
    activeDotColor: Color = Black,
    inactiveDotColor: Color = White,
) {
    val lazyListState: LazyListState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val dotElementContainerSize = remember { getDotContainerSize(type) }
    val bigDotSize = remember { getBigDotSize(type) }
    val smallDotSize = remember { smallDotSize(type) }

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
                if (totalDotsCounter > SMALL_GROUP_MAX_ELEMENT_COUNT) {
                    dotElementContainerSize * MAXIMUM_CONTAINER_SIZE_MULTIPLIER
                } else {
                    dotElementContainerSize * totalDotsCounter
                }
            ),
            contentPadding = PaddingValues(
                horizontal = if (totalDotsCounter > SMALL_GROUP_MAX_ELEMENT_COUNT) {
                    dotElementContainerSize
                } else {
                    0.dp
                }
            ),
            userScrollEnabled = false,
            state = lazyListState,
        ) {
            itemsIndexed(items) { index, item ->
                ViewPagerDot(
                    dotSizeType = getDotSizeType(index, activeDotIndex, totalDotsCounter),
                    isActive = index == activeDotIndex,
                    smallSize = smallDotSize,
                    bigSize = bigDotSize,
                    containerSize = dotElementContainerSize,
                    activeDotColor = activeDotColor,
                    inactiveDotColor = inactiveDotColor
                )
            }
        }
    }
}

fun getDotSizeType(index: Int, activeDot: Int, totalDotsCounter: Int): ViewPagerDotSizeType {
    if (totalDotsCounter <= SMALL_GROUP_MAX_ELEMENT_COUNT) {
        return ViewPagerDotSizeType.BIG
    }
    if (activeDot == 0) {
        return if (index - 2 <= activeDot && index + 1 >= activeDot) {
            ViewPagerDotSizeType.BIG
        } else {
            ViewPagerDotSizeType.SMALL
        }
    }
    if (activeDot == totalDotsCounter - 1) {
        return if (index - 1 <= activeDot && index + 2 >= activeDot) {
            ViewPagerDotSizeType.BIG
        } else {
            ViewPagerDotSizeType.SMALL
        }
    }
    return if (index - 1 <= activeDot && index + 1 >= activeDot) {
        ViewPagerDotSizeType.BIG
    } else {
        ViewPagerDotSizeType.SMALL
    }
}

fun getDotContainerSize(type: ViewPagerIndicatorSizeType): Dp {
    return when (type) {
        ViewPagerIndicatorSizeType.BIG -> 16.dp
        ViewPagerIndicatorSizeType.SMALL -> 10.dp
    }
}

fun getBigDotSize(type: ViewPagerIndicatorSizeType): Dp {
    return when (type) {
        ViewPagerIndicatorSizeType.BIG -> 8.dp
        ViewPagerIndicatorSizeType.SMALL -> 6.dp
    }
}

fun smallDotSize(type: ViewPagerIndicatorSizeType): Dp {
    return when (type) {
        ViewPagerIndicatorSizeType.BIG -> 4.dp
        ViewPagerIndicatorSizeType.SMALL -> 3.dp
    }
}

@Composable
fun ViewPagerDot(
    isActive: Boolean,
    dotSizeType: ViewPagerDotSizeType,
    containerSize: Dp,
    smallSize: Dp,
    bigSize: Dp,
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
                        ViewPagerDotSizeType.BIG -> bigSize
                        ViewPagerDotSizeType.SMALL -> smallSize
                    }
                ), content = {})
    }
}


@Composable
private fun ViewPagerIndicatorGeneralPreview(totalDotsCounter: Int){
    val position = remember { mutableStateOf(0) }
    Column(
        modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ViewPagerIndicator(
            type = ViewPagerIndicatorSizeType.BIG,
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
fun ViewPagerIndicatorPreview_TwoElement() {
    ViewPagerIndicatorGeneralPreview(totalDotsCounter = 2)
}

@Preview
@Composable
fun ViewPagerIndicatorPreview_ThreeElement() {
    ViewPagerIndicatorGeneralPreview(totalDotsCounter = 3)
}

@Preview
@Composable
fun ViewPagerIndicatorPreview_FourElement() {
    ViewPagerIndicatorGeneralPreview(totalDotsCounter = 4)
}

@Preview
@Composable
fun ViewPagerIndicatorPreview_FiveElement() {
    ViewPagerIndicatorGeneralPreview(totalDotsCounter = 5)
}

@Preview
@Composable
fun ViewPagerIndicatorPreview_TenElement() {
    ViewPagerIndicatorGeneralPreview(totalDotsCounter = 10)
}

enum class ViewPagerDotSizeType {
    BIG, SMALL
}

enum class ViewPagerIndicatorSizeType {
    BIG, SMALL
}
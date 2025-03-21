package ua.gov.diia.ui_base.components.atom.text.textMarquee

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.MarqueeAnimationMode
import androidx.compose.foundation.MarqueeSpacing
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ua.gov.diia.ui_base.components.theme.DiiaTextStyle
import kotlin.math.absoluteValue
import kotlin.math.ceil

@Composable
fun MarqueeWithInitialOffset(
    text: String,
    textColor: Color,
    initialOffset: Dp = 16.dp,
    velocity: Dp = 30.dp,
    showOffsetDuration: Long = 300
) {
    val density = LocalDensity.current

    val offsetPx = remember {
        Animatable(with(density) { initialOffset.toPx() })
    }

    val offsetDp by remember {
        derivedStateOf { with(density) { offsetPx.value.toDp() } }
    }

    var isMarqueeRunning by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(Unit) {
        delay(showOffsetDuration)

        val distance = with(density) { initialOffset.toPx() }
        val pxPerMilli = with(density) { velocity.toPx() }.absoluteValue / 1000f
        val duration = ceil(distance / pxPerMilli).toInt()

        // Launch the marquee in advance to avoid delays when changing the animation.
        launch {
            delay((duration * 0.9).toLong())
            isMarqueeRunning = true
        }

        offsetPx.animateTo(
            targetValue = 0F,
            animationSpec = tween(
                durationMillis = duration,
                delayMillis = 0,
                easing = LinearEasing
            )
        )
    }

    Text(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = offsetDp)
            .basicMarquee(
                iterations = Int.MAX_VALUE,
                animationMode = MarqueeAnimationMode.Immediately,
                repeatDelayMillis = 0,
                initialDelayMillis = 0,
                spacing = MarqueeSpacing(0.dp),
                velocity = if (isMarqueeRunning) velocity else 0.dp
            ),
        maxLines = 1,
        style = DiiaTextStyle.t2TextDescription,
        text = text,
        color = textColor
    )
}
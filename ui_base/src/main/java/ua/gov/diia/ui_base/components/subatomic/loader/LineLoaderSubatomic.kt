package ua.gov.diia.ui_base.components.subatomic.loader

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import ua.gov.diia.ui_base.components.theme.GradientAlternativePosition1
import ua.gov.diia.ui_base.components.theme.GradientAlternativePosition2
import ua.gov.diia.ui_base.components.theme.GradientAlternativePosition3
import ua.gov.diia.ui_base.components.theme.GradientAlternativePosition4

private const val LOADING_MAX_SIZE = 100f
private const val LOADING_START = 0f
private const val LOADING_BARRIER_1 = 10f
private const val LOADING_BARRIER_2 = 25f
private const val LOADING_BARRIER_3 = 65f
private const val LOADING_BARRIER_4 = 85f
private const val LOADING_BARRIER_5 = 99f
private const val LOADING_BARRIER_6 = 99f
private const val LOADING_STEP_INTERVAL_MILLIS = 30L
private const val LOADING_SPEED_VERY_SLOW = 0.05f
private const val LOADING_SPEED_SLOW = 0.1f
private const val LOADING_SPEED_NORMAL = 0.2f
private const val LOADING_SPEED_FAST = 0.3f
private const val LOADING_SPEED_VERY_FAST = 0.4f


@Composable
fun LineLoaderSubatomic(
    modifier: Modifier = Modifier
) {
    val progress = remember { mutableStateOf(LOADING_START) }
    LaunchedEffect(Unit) {
        while (progress.value < LOADING_MAX_SIZE) {
            delay(LOADING_STEP_INTERVAL_MILLIS)
            if (progress.value > LOADING_BARRIER_6) {
                progress.value = LOADING_START
            } else {
                when (progress.value) {
                    in LOADING_START..LOADING_BARRIER_1 -> {
                        progress.value = progress.value + LOADING_SPEED_NORMAL
                    }
                    in LOADING_BARRIER_1..LOADING_BARRIER_2 -> {
                        progress.value = progress.value + LOADING_SPEED_FAST
                    }
                    in LOADING_BARRIER_2..LOADING_BARRIER_3 -> {
                        progress.value = progress.value + LOADING_SPEED_NORMAL
                    }
                    in LOADING_BARRIER_3..LOADING_BARRIER_4 -> {
                        progress.value = progress.value + LOADING_SPEED_VERY_FAST
                    }
                    in LOADING_BARRIER_4..LOADING_BARRIER_5 -> {
                        progress.value = progress.value + LOADING_SPEED_VERY_SLOW
                    }
                    else -> {
                        progress.value = progress.value + LOADING_SPEED_SLOW
                    }
                }

            }
        }
    }

    Indicator(
        modifier = modifier.fillMaxWidth(),
        currentProgress = progress.value
    )

}

@Composable
private fun Indicator(
    modifier: Modifier = Modifier,
    currentProgress: Float = LOADING_START
) {
    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(3.dp)
    ) {

        drawLine(
            color = Transparent,
            cap = StrokeCap.Square,
            strokeWidth = size.height,
            start = Offset(x = 0f, y = 0f),
            end = Offset(x = size.width, y = 0f)
        )

        val progress =
            size.width / LOADING_MAX_SIZE * currentProgress

        drawLine(
            brush = Brush.linearGradient(
                colors = listOf(
                    GradientAlternativePosition4,
                    GradientAlternativePosition3,
                    GradientAlternativePosition2,
                    GradientAlternativePosition1
                )
            ),
            cap = StrokeCap.Square,
            strokeWidth = size.height,
            start = Offset(x = 0f, y = 0f),
            end = Offset(x = progress, y = 0f)
        )

    }
}

@Composable
@Preview
fun LineLoaderSubatomicPreview() {
    LineLoaderSubatomic(modifier = Modifier.padding(vertical = 16.dp))
}

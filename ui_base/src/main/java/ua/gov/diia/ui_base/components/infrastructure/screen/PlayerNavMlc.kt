package ua.gov.diia.ui_base.components.infrastructure.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.ui_base.components.theme.DiiaTextStyle
import ua.gov.diia.ui_base.components.theme.GradientAlternativePosition1
import ua.gov.diia.ui_base.components.theme.GradientAlternativePosition2
import ua.gov.diia.ui_base.components.theme.GradientAlternativePosition3
import ua.gov.diia.ui_base.components.theme.GradientAlternativePosition4
import ua.gov.diia.ui_base.components.theme.WhiteAlpha30
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerNavMlc(
    modifier: Modifier = Modifier,
    totalDuration: () -> Long,
    currentTime: () -> Long,
    bufferPercentage: () -> Int,
    onSeekChanged: (timeMs: Float) -> Unit,
    sliderPosition: () -> Float
) {
    val duration = totalDuration().coerceAtLeast(1L)
    val videoTime = currentTime()
    val buffer = bufferPercentage()

    val sliderValue = remember { mutableFloatStateOf(sliderPosition()) }

    LaunchedEffect(videoTime) {
        sliderValue.floatValue = videoTime.toFloat()
    }

    Row(
        modifier = modifier
            .padding(16.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier.weight(1f)) {
            val strokeWidth = 4.dp
            Slider(
                modifier = Modifier
                    .fillMaxWidth()
                    .drawBehind {
                        val sliderWidth = size.width * (sliderValue.floatValue / duration.toFloat())
                        val paddingOffset = 10.dp.toPx()
                        drawLine(
                            brush = Brush.linearGradient(
                                colors = listOf(
                                    GradientAlternativePosition4,
                                    GradientAlternativePosition3,
                                    GradientAlternativePosition2,
                                    GradientAlternativePosition1
                                )
                            ),
                            start = Offset(paddingOffset, size.height / 2),
                            end = Offset(
                                maxOf(paddingOffset, sliderWidth - paddingOffset),
                                size.height / 2
                            ),
                            strokeWidth = strokeWidth.toPx(),
                            cap = StrokeCap.Round
                        )
                        val thumbOffsetX =  maxOf(paddingOffset, sliderWidth - paddingOffset)
                        drawCircle(
                            color = Color.White,
                            radius = 6.dp.toPx(),
                            center = Offset(thumbOffsetX, size.height / 2)
                        )
                    },
                value = sliderValue.floatValue,
                onValueChange = { newValue ->
                    sliderValue.floatValue = newValue
                    onSeekChanged(newValue)
                },
                valueRange = 0f..duration.toFloat(),
                colors = SliderDefaults.colors(
                    activeTrackColor = Color.Transparent,
                    inactiveTrackColor = WhiteAlpha30,
                    activeTickColor = Color.Transparent
                ),
                thumb = {} // Leave empty to hide default thumb
            )
        }

        Text(
            modifier = Modifier
                .padding(start = 16.dp)
                .width(40.dp),
            text = videoTime.formatMinSec(),
            color = Color.White,
            style = DiiaTextStyle.t2TextDescription
        )
    }
}


fun Long.formatMinSec(): String {
    if (this == 0L) return "00:00"

    val minutes = TimeUnit.MILLISECONDS.toMinutes(this)
    val seconds = TimeUnit.MILLISECONDS.toSeconds(this) % 60

    return String.format("%02d:%02d", minutes, seconds)
}

@Preview(showSystemUi = true)
@Composable
fun PlayerNavMlcPreview() {
    val fakeTotalDuration = 300000L
    val fakeCurrentTime = 290000L
    val fakeBufferPercentage = 50

    PlayerNavMlc(
        totalDuration = { fakeTotalDuration },
        currentTime = { fakeCurrentTime },
        bufferPercentage = { fakeBufferPercentage },
        onSeekChanged = { },
        sliderPosition = { fakeCurrentTime.toFloat() }
    )
}
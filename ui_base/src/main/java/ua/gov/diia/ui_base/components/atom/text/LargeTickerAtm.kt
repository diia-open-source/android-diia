package ua.gov.diia.ui_base.components.atom.text

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.core.models.common_compose.atm.text.LargeTickerAtm
import ua.gov.diia.ui_base.components.atom.text.textMarquee.MarqueeWithInitialOffset
import ua.gov.diia.ui_base.components.conditional
import ua.gov.diia.ui_base.components.infrastructure.DataActionWrapper
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.noRippleClickable
import ua.gov.diia.ui_base.components.theme.GrannySmithApple
import ua.gov.diia.ui_base.components.theme.MiddleBlueGreen
import ua.gov.diia.ui_base.components.theme.Neutral
import ua.gov.diia.ui_base.components.theme.RedNegative
import ua.gov.diia.ui_base.components.theme.Transparent
import ua.gov.diia.ui_base.components.theme.WarningYellow
import ua.gov.diia.ui_base.components.theme.gradientBluePosition01
import ua.gov.diia.ui_base.components.theme.gradientBluePosition02
import ua.gov.diia.ui_base.components.theme.gradientColorCyanPosition01
import ua.gov.diia.ui_base.components.theme.gradientColorCyanPosition02
import ua.gov.diia.ui_base.components.theme.gradientColorRainbowPosition01
import ua.gov.diia.ui_base.components.theme.gradientColorRainbowPosition02
import ua.gov.diia.ui_base.components.theme.gradientColorRainbowPosition03
import ua.gov.diia.ui_base.components.theme.gradientColorRainbowPosition04
import ua.gov.diia.ui_base.components.theme.gradientPinkPosition01
import ua.gov.diia.ui_base.components.theme.gradientPinkPosition02
import ua.gov.diia.ui_base.util.gradient.AngleLinearGradient
import ua.gov.diia.ui_base.util.toDataActionWrapper

private const val DEFAULT_LINE_LENGTH = 400

@Composable
fun LargeTickerAtm(
    modifier: Modifier = Modifier,
    data: LargeTickerAtmData,
    onUIAction: (UIAction) -> Unit
) {

    val text = remember { mutableStateOf(makeTextLongForTicker(data.title ?: " ")) }
    val alphaFadeIn = remember { Animatable(0f) }
    val alphaFadeOut = remember { Animatable(1f) }

    LaunchedEffect(key1 = data.oldType) {
        if (data.oldType != null) {
            alphaFadeIn.animateTo(1f, animationSpec = tween(durationMillis = 3000))
            alphaFadeOut.animateTo(0f, animationSpec = tween(3000))
        } else {
            alphaFadeIn.snapTo(1f)
            alphaFadeOut.snapTo(0f)
        }
    }

    LaunchedEffect(key1 = data.title) {
        text.value = if (data.title.isNullOrEmpty()) {
            makeTextLongForTicker(" ")
        } else {
            makeTextLongForTicker(data.title)
        }
    }

    Box(
        modifier = modifier
            .padding(top = 24.dp, bottom = 8.dp)
            .height(32.dp)
            .noRippleClickable {
                if (data.clickable) {
                    onUIAction(UIAction(actionKey = data.actionKey))
                }
            }
            .conditional(data.type == LargeTickerType.POSITIVE || data.oldType == LargeTickerType.POSITIVE) {
                drawBehind {
                    drawRect(
                        brush = Brush.horizontalGradient(
                            0f to GrannySmithApple, 1f to MiddleBlueGreen,
                        ),
                        alpha = if (data.type == LargeTickerType.POSITIVE) alphaFadeIn.value else alphaFadeOut.value
                    )
                }
            }
            .conditional(
                data.type == LargeTickerType.NEUTRAL ||
                        data.type == LargeTickerType.INFORMATIVE ||
                        data.type == LargeTickerType.WARNING ||
                        data.type == LargeTickerType.NEGATIVE
            ) {
                drawBehind {
                    drawRect(
                        when (data.type) {
                            LargeTickerType.NEUTRAL, LargeTickerType.INFORMATIVE -> {
                                Neutral
                            }

                            LargeTickerType.WARNING -> {
                                WarningYellow
                            }

                            LargeTickerType.NEGATIVE -> {
                                RedNegative
                            }

                            else -> {
                                Transparent
                            }
                        },
                        alpha = alphaFadeIn.value
                    )
                }
            }
            .conditional(data.type == LargeTickerType.PINK || data.oldType == LargeTickerType.PINK)
            {
                drawBehind {
                    drawRect(
                        brush = AngleLinearGradient(
                            colors = listOf(gradientPinkPosition01, gradientPinkPosition02),
                            angleInDegrees = 280f
                        ),
                        alpha = if (data.type == LargeTickerType.PINK) alphaFadeIn.value else alphaFadeOut.value
                    )
                }
            }
            .conditional(data.type == LargeTickerType.CYAN || data.oldType == LargeTickerType.CYAN)
            {
                drawBehind {
                    drawRect(
                        brush = AngleLinearGradient(
                            colors = listOf(
                                gradientColorCyanPosition01,
                                gradientColorCyanPosition02
                            ),
                            angleInDegrees = 280f
                        ),
                        alpha = if (data.type == LargeTickerType.CYAN) alphaFadeIn.value else alphaFadeOut.value
                    )
                }
            }
            .conditional(data.type == LargeTickerType.BLUE || data.oldType == LargeTickerType.BLUE)
            {
                drawBehind {
                    drawRect(
                        brush = AngleLinearGradient(
                            colors = listOf(gradientBluePosition01, gradientBluePosition02),
                            angleInDegrees = 280f
                        ),
                        alpha = if (data.type == LargeTickerType.BLUE) alphaFadeIn.value else alphaFadeOut.value
                    )
                }
            }
            .conditional(data.type == LargeTickerType.RAINBOW || data.oldType == LargeTickerType.RAINBOW)
            {
                drawBehind {
                    drawRect(
                        brush = AngleLinearGradient(
                            colors = listOf(
                                gradientColorRainbowPosition01, gradientColorRainbowPosition02,
                                gradientColorRainbowPosition03, gradientColorRainbowPosition04
                            ),
                            angleInDegrees = 280f
                        ),
                        alpha = if (data.type == LargeTickerType.RAINBOW) alphaFadeIn.value else alphaFadeOut.value
                    )
                }
            }
            .conditional(
                data.oldType == LargeTickerType.NEUTRAL ||
                        data.oldType == LargeTickerType.INFORMATIVE ||
                        data.oldType == LargeTickerType.WARNING ||
                        data.oldType == LargeTickerType.NEGATIVE
            ) {
                drawBehind {
                    drawRect(
                        when (data.oldType) {
                            LargeTickerType.INFORMATIVE, LargeTickerType.NEUTRAL -> {
                                Neutral
                            }

                            LargeTickerType.WARNING -> {
                                WarningYellow
                            }

                            LargeTickerType.NEGATIVE -> {
                                RedNegative
                            }

                            else -> {
                                Transparent
                            }
                        },
                        alpha = alphaFadeOut.value
                    )
                }
            }
            .semantics {
                testTag = data.componentId
            },
        contentAlignment = Alignment.Center
    ) {
        MarqueeWithInitialOffset(
            text = text.value,
            textColor = if (data.type == LargeTickerType.NEGATIVE || data.type == LargeTickerType.CYAN) Color.White else Color.Black
        )
    }
}

data class LargeTickerAtmData(
    val actionKey: String = UIActionKeysCompose.LARGE_TICKER_ATOM_CLICK,
    val id: String = "",
    val componentId: String = "",
    val title: String? = null,
    val type: LargeTickerType,
    val state: String? = null,
    val clickable: Boolean = false,
    val action: DataActionWrapper? = null,
    val oldType: LargeTickerType? = null,
) : UIElementData {

    fun animateToNewColorByCopy(type: LargeTickerType): LargeTickerAtmData {
        return this.copy(oldType = this.type, type = type)
    }

}

enum class LargeTickerType(val type: String) {
    INFORMATIVE("informative"),
    BLUE("blue"),
    PINK("pink"),
    RAINBOW("rainbow"),
    WARNING("warning"),
    NEGATIVE("negative"),
    POSITIVE("positive"),
    NEUTRAL("neutral"),
    CYAN("cyan")
}

fun LargeTickerAtm.toUiModel(): LargeTickerAtmData {
    return LargeTickerAtmData(
        componentId = componentId.orEmpty(),
        title = value,
        type = when (type) {
            LargeTickerAtm.TickerType.informative -> LargeTickerType.INFORMATIVE
            LargeTickerAtm.TickerType.blue -> LargeTickerType.BLUE
            LargeTickerAtm.TickerType.pink -> LargeTickerType.PINK
            LargeTickerAtm.TickerType.rainbow -> LargeTickerType.RAINBOW
            LargeTickerAtm.TickerType.warning -> LargeTickerType.WARNING
            LargeTickerAtm.TickerType.negative -> LargeTickerType.NEGATIVE
            LargeTickerAtm.TickerType.positive -> LargeTickerType.POSITIVE
            LargeTickerAtm.TickerType.neutral -> LargeTickerType.NEUTRAL
            LargeTickerAtm.TickerType.cyan -> LargeTickerType.CYAN
        },
        action = action?.toDataActionWrapper()
    )
}

private fun makeTextLongForTicker(text: String): String {
    val expectedLineLength = DEFAULT_LINE_LENGTH
    if (text.isEmpty()) {
        return text
    }
    val baseRepeatCount = expectedLineLength / text.length
    val adjustedRepeatCount =
        if (expectedLineLength % text.length == 0) baseRepeatCount + 1 else baseRepeatCount
    return "${text.trimEnd()} • ".repeat(adjustedRepeatCount)
}

fun generateLargeTickerAtmMockData(tickerType: LargeTickerType): LargeTickerAtmData {
    return when (tickerType) {
        LargeTickerType.POSITIVE -> LargeTickerAtmData(
            title = "Positive Positive Positive Positive Positive",
            type = LargeTickerType.POSITIVE
        )

        LargeTickerType.WARNING -> LargeTickerAtmData(
            title = "Warning Warning Warning Warning Warning",
            type = LargeTickerType.WARNING
        )

        LargeTickerType.INFORMATIVE -> LargeTickerAtmData(
            title = "Informative Informative Informative Informative Informative",
            type = LargeTickerType.INFORMATIVE
        )

        LargeTickerType.NEUTRAL -> LargeTickerAtmData(
            title = "Neutral Neutral Neutral Neutral Neutral",
            type = LargeTickerType.NEUTRAL
        )

        LargeTickerType.NEGATIVE -> LargeTickerAtmData(
            title = "Negative Negative Negative Negative Negative",
            type = LargeTickerType.NEGATIVE
        )

        LargeTickerType.RAINBOW -> LargeTickerAtmData(
            title = "Rainbow Rainbow Rainbow Rainbow Rainbow ",
            type = LargeTickerType.RAINBOW
        )

        LargeTickerType.CYAN -> LargeTickerAtmData(
            title = "Cyan Cyan Cyan Cyan Cyan Cyan",
            type = LargeTickerType.CYAN
        )

        LargeTickerType.PINK -> LargeTickerAtmData(
            title = "Pink Pink Pink Pink Pink Pink",
            type = LargeTickerType.PINK
        )

        LargeTickerType.BLUE -> LargeTickerAtmData(
            title = "Blue Blue Blue Blue Blue Blue Blue ",
            type = LargeTickerType.BLUE
        )
    }

}

@Preview
@Composable
fun LargeTickerAtmInformativePreview() {
    LargeTickerAtm(data = generateLargeTickerAtmMockData(LargeTickerType.INFORMATIVE)) {}
}

@Preview
@Composable
fun LargeTickerAtmBluePreview() {
    LargeTickerAtm(data = generateLargeTickerAtmMockData(LargeTickerType.BLUE)) {}
}

@Preview
@Composable
fun LargeTickerAtmPinkPreview() {
    LargeTickerAtm(data = generateLargeTickerAtmMockData(LargeTickerType.PINK)) {}
}

@Preview
@Composable
fun LargeTickerAtmRainbowPreview() {
    LargeTickerAtm(data = generateLargeTickerAtmMockData(LargeTickerType.RAINBOW)) {}
}

@Preview
@Composable
fun LargeTickerAtmWarningPreview() {
    LargeTickerAtm(data = generateLargeTickerAtmMockData(LargeTickerType.WARNING)) {}
}

@Preview
@Composable
fun LargeTickerAtmNegativePreview() {
    LargeTickerAtm(data = generateLargeTickerAtmMockData(LargeTickerType.NEGATIVE)) {}
}

@Preview
@Composable
fun LargeTickerAtmPositivePreview() {
    LargeTickerAtm(data = generateLargeTickerAtmMockData(LargeTickerType.POSITIVE)) {}
}

@Preview
@Composable
fun LargeTickerAtmNeutralPreview() {
    LargeTickerAtm(data = generateLargeTickerAtmMockData(LargeTickerType.NEUTRAL)) {}
}

@Preview
@Composable
fun LargeTickerAtmCyanPreview() {
    LargeTickerAtm(data = generateLargeTickerAtmMockData(LargeTickerType.CYAN)) {}
}
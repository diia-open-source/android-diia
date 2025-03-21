package ua.gov.diia.ui_base.components.atom.text

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
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

private const val DEFAULT_LINE_LENGTH = 400

@Composable
fun TickerAtm(
    modifier: Modifier = Modifier,
    data: TickerAtomData,
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
            .height(
                when (data.usage) {
                    TickerUsage.GRAND -> 32.dp
                    else -> 24.dp
                }
            )
            .noRippleClickable {
                if (data.clickable) {
                    onUIAction(UIAction(actionKey = data.actionKey))
                }
            }
            .conditional(data.type == TickerType.POSITIVE || data.oldType == TickerType.POSITIVE) {
                drawBehind {
                    drawRect(
                        brush = Brush.horizontalGradient(
                            0f to GrannySmithApple, 1f to MiddleBlueGreen,
                        ),
                        alpha = if (data.type == TickerType.POSITIVE) alphaFadeIn.value else alphaFadeOut.value
                    )
                }
            }
            .conditional(
                data.type == TickerType.NEUTRAL ||
                        data.type == TickerType.INFORMATIVE ||
                        data.type == TickerType.WARNING ||
                        data.type == TickerType.NEGATIVE
            ) {
                drawBehind {
                    drawRect(
                        when (data.type) {
                            TickerType.NEUTRAL, TickerType.INFORMATIVE -> {
                                Neutral
                            }

                            TickerType.WARNING -> {
                                WarningYellow
                            }

                            TickerType.NEGATIVE -> {
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
            .conditional(data.type == TickerType.PINK || data.oldType == TickerType.PINK)
            {
                drawBehind {
                    drawRect(
                        brush = AngleLinearGradient(
                            colors = listOf(gradientPinkPosition01, gradientPinkPosition02),
                            angleInDegrees = 280f
                        ),
                        alpha = if (data.type == TickerType.PINK) alphaFadeIn.value else alphaFadeOut.value
                    )
                }
            }
            .conditional(data.type == TickerType.CYAN || data.oldType == TickerType.CYAN)
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
                        alpha = if (data.type == TickerType.CYAN) alphaFadeIn.value else alphaFadeOut.value
                    )
                }
            }
            .conditional(data.type == TickerType.BLUE || data.oldType == TickerType.BLUE)
            {
                drawBehind {
                    drawRect(
                        brush = AngleLinearGradient(
                            colors = listOf(gradientBluePosition01, gradientBluePosition02),
                            angleInDegrees = 280f
                        ),
                        alpha = if (data.type == TickerType.BLUE) alphaFadeIn.value else alphaFadeOut.value
                    )
                }
            }
            .conditional(data.type == TickerType.RAINBOW || data.oldType == TickerType.RAINBOW)
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
                        alpha = if (data.type == TickerType.RAINBOW) alphaFadeIn.value else alphaFadeOut.value
                    )
                }
            }
            .conditional(
                data.oldType == TickerType.NEUTRAL ||
                        data.oldType == TickerType.INFORMATIVE ||
                        data.oldType == TickerType.WARNING ||
                        data.oldType == TickerType.NEGATIVE
            ) {
                drawBehind {
                    drawRect(
                        when (data.oldType) {

                            TickerType.INFORMATIVE, TickerType.NEUTRAL -> {
                                Neutral
                            }

                            TickerType.WARNING -> {
                                WarningYellow
                            }

                            TickerType.NEGATIVE -> {
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
            textColor = if (data.type == TickerType.NEGATIVE || data.type == TickerType.CYAN) Color.White else Color.Black
        )
    }
}

data class TickerAtomData(
    val actionKey: String = UIActionKeysCompose.TICKER_ATOM_CLICK,
    val id: String = "",
    val componentId: String = "",
    val title: String? = null,
    val type: TickerType,
    val usage: TickerUsage,
    val state: String? = null,
    val clickable: Boolean = false,
    val action: DataActionWrapper? = null,
    val oldType: TickerType? = null
) : UIElementData {

    fun animateToNewColorByCopy(type: TickerType): TickerAtomData {
        return this.copy(oldType = this.type, type = type)
    }
}

enum class TickerType(val type: String) {
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

enum class TickerUsage {
    GRAND, BASE, DOCUMENT, STACKED_CARD
}

private fun makeTextLongForTicker(text: String): String {
    val expectedLineLength = DEFAULT_LINE_LENGTH
    if (text.isEmpty()) {
        return text
    }
    val baseRepeatCount = expectedLineLength / text.length
    val adjustedRepeatCount =
        if (expectedLineLength % text.length == 0) baseRepeatCount + 1 else baseRepeatCount
    return "${text.trimEnd()} ".repeat(adjustedRepeatCount)
}

fun generateTickerAtmMockData(
    title: String = "Mocked title, Mocked title, Mocked title",
    type: TickerType,
    usage: TickerUsage = TickerUsage.BASE,
): TickerAtomData {
    return TickerAtomData(title = title, type = type, usage = usage)
}

@Preview
@Composable
fun TickerAtmBaseInformativePreview() {
    TickerAtm(
        data = generateTickerAtmMockData(
            title = "Small informative",
            type = TickerType.INFORMATIVE,
            usage = TickerUsage.BASE
        )
    ) {}
}

@Preview
@Composable
fun TickerAtmGrandInformativePreview() {
    TickerAtm(
        data = generateTickerAtmMockData(
            title = "Small informative",
            type = TickerType.INFORMATIVE,
            usage = TickerUsage.GRAND
        )
    ) {}
}

@Preview
@Composable
fun TickerAtmBaseBluePreview() {
    TickerAtm(
        data = generateTickerAtmMockData(
            title = "Документ створено для покращення демографічної ситуації",
            type = TickerType.BLUE,
            usage = TickerUsage.BASE

        )
    ) {}
}

@Preview
@Composable
fun TickerAtmGrandBluePreview() {
    TickerAtm(
        data = generateTickerAtmMockData(
            title = "Документ створено для покращення демографічної ситуації",
            type = TickerType.BLUE,
            usage = TickerUsage.GRAND

        )
    ) {}
}

@Preview
@Composable
fun TickerAtmBasePinkPreview() {
    TickerAtm(
        data = generateTickerAtmMockData(
            title = "No internet",
            type = TickerType.PINK,
            usage = TickerUsage.BASE
        )
    ) {}
}

@Preview
@Composable
fun TickerAtmGrandPinkPreview() {
    TickerAtm(
        data = generateTickerAtmMockData(
            title = "No internet",
            type = TickerType.PINK,
            usage = TickerUsage.GRAND
        )
    ) {}
}

@Preview
@Composable
fun TickerAtmBaseRainbowPreview() {
    TickerAtm(
        data = generateTickerAtmMockData(
            title = "Документ створено для покращення демографічної ситуації",
            type = TickerType.RAINBOW,
            usage = TickerUsage.BASE
        )
    ) {}
}

@Preview
@Composable
fun TickerAtmBaseWarningPreview() {
    TickerAtm(
        data = generateTickerAtmMockData(
            title = "No internet",
            type = TickerType.WARNING,
            usage = TickerUsage.BASE
        )
    ) {}
}

@Preview
@Composable
fun TickerAtmBaseNegativePreview() {
    TickerAtm(
        data = generateTickerAtmMockData(
            title = "No internet",
            type = TickerType.NEGATIVE,
            usage = TickerUsage.BASE
        )
    ) {}
}

@Preview
@Composable
fun TickerAtmBasePositivePreview() {
    TickerAtm(
        data = generateTickerAtmMockData(
            title = "No internet",
            type = TickerType.POSITIVE,
            usage = TickerUsage.BASE
        )
    ) {}
}

@Preview
@Composable
fun TickerAtmBaseNeutralPreview() {
    TickerAtm(
        data = generateTickerAtmMockData(
            title = "No internet",
            type = TickerType.NEUTRAL,
            usage = TickerUsage.BASE
        )
    ) {}
}

@Preview
@Composable
fun TickerAtmBaseCyanPreview() {
    TickerAtm(
        data = generateTickerAtmMockData(
            title = "No internet",
            type = TickerType.CYAN,
            usage = TickerUsage.BASE
        )
    ) {}
}
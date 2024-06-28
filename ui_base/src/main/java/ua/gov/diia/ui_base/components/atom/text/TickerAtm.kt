package ua.gov.diia.ui_base.components.atom.text


import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.MarqueeSpacing
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
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
import ua.gov.diia.ui_base.components.conditional
import ua.gov.diia.ui_base.components.infrastructure.DataActionWrapper
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.noRippleClickable
import ua.gov.diia.ui_base.components.theme.BrightGray
import ua.gov.diia.ui_base.components.theme.Bubbles
import ua.gov.diia.ui_base.components.theme.DiiaTextStyle
import ua.gov.diia.ui_base.components.theme.GrannySmithApple
import ua.gov.diia.ui_base.components.theme.MiddleBlueGreen
import ua.gov.diia.ui_base.components.theme.RedNegative
import ua.gov.diia.ui_base.components.theme.Transparent
import ua.gov.diia.ui_base.components.theme.WarningYellow

private const val DEFAULT_LINE_LENGTH = 300

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TickerAtm(
    modifier: Modifier = Modifier,
    data: TickerAtomData,
    onUIAction: (UIAction) -> Unit
) {

    val text = remember { mutableStateOf(makeTextLongForTicker(data.title ?: " ")) }

    LaunchedEffect(key1 = data.title) {
        text.value = makeTextLongForTicker(data.title ?: " ")
    }
    LaunchedEffect(key1 = data.title) {
        data.title?.let { text.value = it }
    }

    Box(
        modifier = modifier
            .height(
                when (data.type) {
                    TickerType.BIG_POSITIVE,
                    TickerType.BIG_NEUTRAL,
                    TickerType.BIG_WARNING -> 32.dp

                    TickerType.SMALL_WARNING,
                    TickerType.SMALL_POSITIVE,
                    TickerType.SMALL_NEUTRAL,
                    TickerType.SMALL_NEGATIVE,
                    TickerType.SMALL_QUESTIONNAIRE,
                    TickerType.SMALL_INFORMATIVE -> 24.dp
                }
            )
            .noRippleClickable {
                if (data.clickable) {
                    onUIAction(UIAction(actionKey = data.actionKey))

                }
            }
            .conditional(
                data.type == TickerType.BIG_POSITIVE ||
                        data.type == TickerType.SMALL_POSITIVE
            ) {
                drawBehind {
                    drawRect(
                        brush = Brush.horizontalGradient(
                            0f to GrannySmithApple, 1f to MiddleBlueGreen,
                        )
                    )
                }
            }
            .conditional(
                data.type == TickerType.BIG_WARNING ||
                        data.type == TickerType.SMALL_NEUTRAL ||
                        data.type == TickerType.BIG_NEUTRAL ||
                        data.type == TickerType.SMALL_POSITIVE ||
                        data.type == TickerType.SMALL_WARNING ||
                        data.type == TickerType.SMALL_QUESTIONNAIRE ||
                        data.type == TickerType.SMALL_NEGATIVE ||
                        data.type == TickerType.SMALL_INFORMATIVE
            ) {
                drawBehind {
                    drawRect(
                        when (data.type) {
                            TickerType.BIG_WARNING,
                            TickerType.SMALL_WARNING -> {
                                WarningYellow
                            }


                            TickerType.SMALL_NEUTRAL, TickerType.BIG_NEUTRAL -> {
                                BrightGray
                            }

                            TickerType.SMALL_NEGATIVE -> {
                                RedNegative
                            }

                            TickerType.SMALL_QUESTIONNAIRE, TickerType.SMALL_INFORMATIVE -> {
                                Bubbles
                            }

                            else -> {
                                Transparent
                            }
                        }
                    )
                }
            }
            .semantics {
                testTag = data.componentId
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            modifier = Modifier
                .basicMarquee(
                    iterations = Int.MAX_VALUE,
                    delayMillis = 0,
                    spacing = MarqueeSpacing(0.dp)
                )
                .fillMaxWidth(),
            maxLines = 1,
            style = DiiaTextStyle.t2TextDescription,
            text = text.value,
            color = if (data.type == TickerType.SMALL_NEGATIVE) Color.White else Color.Black
        )
    }
}

data class TickerAtomData(
    val actionKey: String = UIActionKeysCompose.TICKER_ATOM_CLICK,
    val id: String = "",
    val componentId: String = "",
    val title: String? = null,
    val type: TickerType,
    val state: String? = null,
    val clickable: Boolean = false,
    val action: DataActionWrapper? = null
) : UIElementData

enum class TickerType {
    BIG_POSITIVE,
    BIG_WARNING,
    BIG_NEUTRAL,

    SMALL_WARNING,
    SMALL_POSITIVE,
    SMALL_NEUTRAL,
    SMALL_INFORMATIVE,
    SMALL_NEGATIVE,
    SMALL_QUESTIONNAIRE
}

private fun makeTextLongForTicker(text: String): String {
    val expectedLineLength = DEFAULT_LINE_LENGTH
    if (text.isEmpty()) {
        return text
    }
    val multiplier: Int = expectedLineLength / text.length
    return if (multiplier > 1) {
        "$text ".repeat(multiplier)
    } else {
        "$text "
    }
}

@Composable
@Preview
fun TickerAtmPreview() {
    val state = TickerAtomData(title = "Hello world", type = TickerType.BIG_POSITIVE)
    TickerAtm(data = state) {}
}

@Preview
@Composable
fun TickerAtm_BigPositivePreview() {
    val data = TickerAtomData(title = "No internet", type = TickerType.BIG_POSITIVE)
    TickerAtm(data = data) {}
}

@Preview
@Composable
fun TickerAtmBigWarningPreview() {
    val data = TickerAtomData(title = "No internet", type = TickerType.BIG_WARNING)

    TickerAtm(data = data) {}
}

@Preview
@Composable
fun TickerAtmSmallWarningPreview() {
    val data = TickerAtomData(title = "No internet", type = TickerType.SMALL_WARNING)

    TickerAtm(data = data) {}
}

@Preview
@Composable
fun TickerAtmSmallInformativePreview() {
    val data = TickerAtomData(title = "Small informative", type = TickerType.SMALL_INFORMATIVE)

    TickerAtm(data = data) {}
}

@Preview
@Composable
fun TickerAtmSmallPositivePreview() {
    val data = TickerAtomData(title = "No internet", type = TickerType.SMALL_POSITIVE)

    TickerAtm(data = data) {}
}


@Preview
@Composable
fun TickerAtmSmallNeutralPreview() {
    val data = TickerAtomData(title = "No internet", type = TickerType.SMALL_NEUTRAL)

    TickerAtm(data = data) {}
}

@Preview
@Composable
fun TickerAtmBigNeutralPreview() {
    val data = TickerAtomData(title = "No internet", type = TickerType.SMALL_NEGATIVE)

    TickerAtm(data = data) {}
}

@Preview
@Composable
fun TickerAtmQuestionnaireTempPreview() {
    val data = TickerAtomData(title = "No internet", type = TickerType.SMALL_QUESTIONNAIRE)

    TickerAtm(data = data) {}
}
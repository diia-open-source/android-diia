package ua.gov.diia.ui_base.components.molecule.text

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ua.gov.diia.core.models.common_compose.mlc.text.TimerMlc
import ua.gov.diia.ui_base.components.atom.button.BtnLinkAtm
import ua.gov.diia.ui_base.components.atom.button.BtnLinkAtmData
import ua.gov.diia.ui_base.components.atom.button.toUIModel
import ua.gov.diia.ui_base.components.atom.text.textwithparameter.TextParameter
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.toDynamicString
import ua.gov.diia.ui_base.components.theme.BlackAlpha30
import ua.gov.diia.ui_base.components.theme.DiiaTextStyle

@Composable
fun TimerMlc(
    modifier: Modifier = Modifier,
    data: TimerMlcData,
    style: TextStyle = DiiaTextStyle.t3TextBody,
    onUIAction: (UIAction) -> Unit
) {

    Column {
        if (data.timer != 0 && data.isExpired == false) {
            Column(
                modifier = Modifier
                    .padding(top = 24.dp),
            ) {
                TimerText(
                    style = style,
                    expired = data.isExpired ?: true,
                    timer = data.timer,
                    labelFirst = data.expireLabelFirst.asString(),
                    labelLast = data.expireLabelLast?.asString(),
                    id = data.id,
                    onUIAction = onUIAction
                )
            }
        } else {
            data.btnLink?.let {
                BtnLinkAtm(
                    data = it,
                    onUIAction = onUIAction
                )
            }
        }
    }
}

data class TimerMlcData(
    val id: String? = "",
    val componentId: UiText? = null,
    val actionKey: String = UIActionKeysCompose.TIMER_MLC,
    val expireLabelFirst: UiText,
    val expireLabelLast: UiText? = null,
    val timer: Int,
    val btnLink: BtnLinkAtmData? = null,
    val parameters: List<TextParameter>? = null,
    var isExpired: Boolean? = false,
) : UIElementData {

    fun timeExpired(newValue: Boolean): TimerMlcData {
        return this.copy(
            isExpired = newValue,
        )
    }

}

@Composable
fun TimerText(
    style: TextStyle,
    expired: Boolean,
    timer: Int,
    labelFirst: String,
    labelLast: String?,
    id: String?,
    onUIAction: (UIAction) -> Unit
) {
    var minutes by remember { mutableIntStateOf(timer / 60) }
    var seconds by remember { mutableIntStateOf(timer % 60) }

    LaunchedEffect(expired) {
        if (!expired) {
            minutes = timer / 60
            seconds = timer % 60
            launch {
                while (minutes > 0 || seconds > 0) {
                    delay(1000)
                    if (seconds == 0) {
                        minutes--
                        seconds = 59
                    } else {
                        seconds--
                    }
                }
                onUIAction(
                    UIAction(
                        actionKey = UIActionKeysCompose.TEXT_LABEL_MLC_TIMER_IS_EXPIRED,
                        data = id,
                    )
                )
            }
        }
    }
    if (!expired) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp)
        ) {
            Text(
                text = labelFirst,
                style = style,
                color = BlackAlpha30
            )
            Text(
                modifier = Modifier.width(32.dp),
                text = "${minutes}:${String.format("%02d", seconds)}",
                textAlign = TextAlign.Center,
                style = style,
                color = BlackAlpha30
            )
            labelLast?.let { ll ->
                Text(
                    text = ll,
                    style = style,
                    color = BlackAlpha30
                )
            }
        }
    }
}

fun TimerMlc?.toUIModel(): TimerMlcData? {
    val entity = this
    if (entity?.expireLabel == null) return null
    return TimerMlcData(
        componentId = UiText.DynamicString(this?.componentId.orEmpty()),
        id = "Timer_Mlc",
        expireLabelFirst = entity.expireLabel?.expireLabelFirst.toDynamicString(),
        expireLabelLast = entity.expireLabel?.expireLabelLast.toDynamicString(),
        timer = entity.expireLabel?.timer ?: 0,
        btnLink = entity.stateAfterExpiration.btnLinkAtm.toUIModel(),
    )
}


@Preview
@Composable
fun TimerMlcPreview() {
    val timerMlcData = TimerMlcData(
        expireLabelFirst = UiText.DynamicString("There is still time before vacation"),
        expireLabelLast = UiText.DynamicString("min"),
        timer = 120,
    )
    TimerMlc(
        data = timerMlcData
    ) {
    }
}

@Preview
@Composable
fun TimerMlc_Expired_Preview() {
    val timerMlcData = TimerMlcData(
        expireLabelFirst = UiText.DynamicString("There is still time before vacation"),
        expireLabelLast = UiText.DynamicString("min"),
        timer = 120,
        btnLink = BtnLinkAtmData(
            title = UiText.DynamicString("Resend")
        ),
        isExpired = true
    )
    TimerMlc(
        data = timerMlcData
    ) {
    }
}
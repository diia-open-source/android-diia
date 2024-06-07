package ua.gov.diia.ui_base.components.subatomic.timer

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.toDynamicString
import ua.gov.diia.ui_base.components.theme.BlackAlpha40
import ua.gov.diia.ui_base.components.theme.DiiaTextStyle

@Composable
fun ExpireLabel(
    modifier: Modifier = Modifier,
    expireLabelFirst: UiText,
    expireLabelLast: UiText? = null,
    timer: Int,
    expired: Boolean,
    onTimeOver: () -> Unit
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
                onTimeOver()
            }
        }
    }

    Row(
        modifier = modifier,
    ) {
        Text(
            text = expireLabelFirst.asString(),
            style = DiiaTextStyle.t4TextSmallDescription,
            color = BlackAlpha40
        )
        Text(
            modifier = Modifier
                .width(32.dp),
            text = if (expired) {
                " 0:00 "
            } else {
                " ${minutes}:${String.format("%02d", seconds)} "
            },
            textAlign = TextAlign.Center,
            style = DiiaTextStyle.t4TextSmallDescription,
            color = BlackAlpha40
        )
        expireLabelLast?.let {
            Text(
                text = expireLabelLast.asString(),
                style = DiiaTextStyle.t4TextSmallDescription,
                color = BlackAlpha40
            )
        }
    }
}

@Preview
@Composable
fun ExpireLabel_Preview() {
    var expired by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ExpireLabel(
            expireLabelFirst = "Код діятиме".toDynamicString(),
            timer = 120,
            expireLabelLast = "хв".toDynamicString(),
            expired = expired
        ) {
            expired = true
        }

        Button(modifier = Modifier.padding(top = 16.dp),
            onClick = {
                expired = false
            }) {
            Text("Reset")
        }
    }
}
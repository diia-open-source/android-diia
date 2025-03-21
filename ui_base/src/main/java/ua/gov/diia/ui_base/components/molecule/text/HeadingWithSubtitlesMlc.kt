package ua.gov.diia.ui_base.components.molecule.text

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.utils.AutoSizeLimitedText
import ua.gov.diia.ui_base.components.theme.Black
import ua.gov.diia.ui_base.components.theme.DiiaTextStyle

@Composable
fun HeadingWithSubtitlesMlc(
    modifier: Modifier = Modifier,
    data: HeadingWithSubtitlesMlcData,
    onUIAction: (UIAction) -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .semantics {
                testTag = data.componentId
            }
    ) {
        data.value?.let {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
                    .align(alignment = Alignment.Start)
            ) {
                AutoSizeLimitedText(text = data.value, modifier = Modifier, maxLines = 3)
            }
        }

        if (!data.subtitles.isNullOrEmpty()) {
            data.subtitles.forEach {
                Text(
                    modifier = Modifier.padding(top = 16.dp),
                    text = it,
                    style = DiiaTextStyle.t1BigText,
                    color = Black
                )
            }
        }
    }
}

data class HeadingWithSubtitlesMlcData(
    val componentId: String = "",
    val value: String?,
    val subtitles: List<String>?,
)

@Composable
@Preview
fun HeadingWithSubtitlesMlcPreview() {

    val subtitles = listOf("Закорднонний паспорт", "Ukraine • Україна")
    val data = HeadingWithSubtitlesMlcData(
        value = "Паспорт громадянинаПаспорт громадянинаПаспорт громадянинаПаспорт громадянинаПаспорт громадянина",
        subtitles = subtitles
    )

    HeadingWithSubtitlesMlc(data = data)
}
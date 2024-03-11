package ua.gov.diia.ui_base.components.molecule.text

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.theme.DiiaTextStyle

@Composable
fun TitleLabelMlc(
    modifier: Modifier = Modifier,
    data: TitleLabelMlcData
) {
    Text(
        modifier = modifier
            .padding(horizontal = 24.dp)
            .padding(top = 8.dp)
            .fillMaxWidth(),
        text = data.label,
        style = DiiaTextStyle.h1Heading
    )
}

data class TitleLabelMlcData(
    val actionKey: String = UIActionKeysCompose.TEXT_LABEL_MLC,
    val label: String,
): UIElementData

@Preview
@Composable
fun TitleLabelMlcPreview() {
    TitleLabelMlc(
        data = TitleLabelMlcData(label = "Продаж транспортного засобу")
    )
}
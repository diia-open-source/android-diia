package ua.gov.diia.ui_base.components.molecule.text

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.ui_base.components.conditional
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.subatomic.icon.IconBase64Subatomic
import ua.gov.diia.ui_base.components.subatomic.preview.PreviewBase64Images
import ua.gov.diia.ui_base.components.theme.Black
import ua.gov.diia.ui_base.components.theme.BlackAlpha30
import ua.gov.diia.ui_base.components.theme.DiiaTextStyle

@Composable
fun DetailsTextLabelMolecule(
    modifier: Modifier = Modifier,
    data: DetailsTextLabelMoleculeData,
    onUIAction: (UIAction) -> Unit
) {

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        data.base64Image?.let {
            IconBase64Subatomic(modifier = Modifier.size(64.dp), base64Image = data.base64Image)
        }
        Column(modifier = Modifier
            .conditional(data.base64Image != null) {
                padding(start = 12.dp)
            }) {
            Text(
                text = data.title,
                style = DiiaTextStyle.t2TextDescription,
                color = Black
            )
            Spacer(modifier = Modifier.height(height = 4.dp))
            Text(
                text = data.description,
                style = DiiaTextStyle.t2TextDescription,
                color = BlackAlpha30
            )
        }
    }
}

data class DetailsTextLabelMoleculeData(val title: String, val description: String, val base64Image: String? = null) : DetailsText

@Composable
@Preview
fun DetailsTextLabelMoleculePreview_WithImage() {
    val state = DetailsTextLabelMoleculeData(title = "Title", description = "Description", base64Image = PreviewBase64Images.alphaBank)
    DetailsTextLabelMolecule(modifier = Modifier.fillMaxWidth(), data = state) {

    }
}

@Composable
@Preview
fun DetailsTextLabelMoleculePreview_NoImage() {
    val state = DetailsTextLabelMoleculeData(title = "Title", description = "Description")
    DetailsTextLabelMolecule(modifier = Modifier.fillMaxWidth(), data = state) {

    }
}

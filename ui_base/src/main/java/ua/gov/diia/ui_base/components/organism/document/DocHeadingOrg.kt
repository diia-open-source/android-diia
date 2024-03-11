package ua.gov.diia.ui_base.components.organism.document

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.tooling.preview.Preview
import ua.gov.diia.ui_base.R
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.molecule.doc.DocNumberCopyMlc
import ua.gov.diia.ui_base.components.molecule.doc.DocNumberCopyMlcData
import ua.gov.diia.ui_base.components.molecule.doc.DocNumberCopyWhiteMlc
import ua.gov.diia.ui_base.components.molecule.doc.DocNumberCopyWhiteMlcData
import ua.gov.diia.ui_base.components.molecule.text.HeadingWithSubtitlesMlc
import ua.gov.diia.ui_base.components.molecule.text.HeadingWithSubtitlesMlcData
import ua.gov.diia.ui_base.components.molecule.text.HeadingWithSubtitlesWhiteMlc
import ua.gov.diia.ui_base.components.molecule.text.HeadingWithSubtitlesWhiteMlcData

@Composable
fun DocHeadingOrg(
    modifier: Modifier = Modifier,
    data: DocHeadingOrgData,
    onUIAction: (UIAction) -> Unit
) {
    Row(modifier = modifier.fillMaxWidth().semantics {
        testTag = data.componentId
    }) {
        Column(modifier = Modifier.weight(1f)) {
            data.heading?.let { HeadingWithSubtitlesMlc(data = it) }
            data.headingWhite?.let { HeadingWithSubtitlesWhiteMlc(data = it) }
            data.docNumber?.let { DocNumberCopyMlc(data = it, onUIAction = onUIAction) }
            data.docNumberCopyWhite?.let { DocNumberCopyWhiteMlc(data = it, onUIAction = onUIAction)}

        }
    }
}

data class DocHeadingOrgData(
    val id: String? = null,
    val componentId: String = "",
    val heading: HeadingWithSubtitlesMlcData? = null,
    val headingWhite: HeadingWithSubtitlesWhiteMlcData? = null,
    val docNumber: DocNumberCopyMlcData? = null,
    val docNumberCopyWhite: DocNumberCopyWhiteMlcData? = null,
) : UIElementData

@Composable
@Preview
fun DocHeadingOrgPreview() {
    val data = DocHeadingOrgData(
        id = "123",
        heading = HeadingWithSubtitlesMlcData(
            value = "Паспорт громадянина\nУкраїни\u2028",
            subtitles = listOf("Закорднонний паспорт", "Ukraine • Україна")
        ),
        docNumber = DocNumberCopyMlcData(
            value = "1234567890",
            icon = UiText.StringResource(R.drawable.ic_copy_settings)
        ),
    )
    DocHeadingOrg(data = data) {}
}
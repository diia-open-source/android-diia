package ua.gov.diia.ui_base.components.organism.document

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.core.models.common_compose.org.doc.DocHeadingOrg
import ua.gov.diia.ui_base.components.DiiaResourceIcon
import ua.gov.diia.ui_base.components.atom.icon.IconAtmData
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.toDynamicString
import ua.gov.diia.ui_base.components.molecule.doc.DocNumberCopyMlc
import ua.gov.diia.ui_base.components.molecule.doc.DocNumberCopyMlcData
import ua.gov.diia.ui_base.components.molecule.doc.DocNumberCopyWhiteMlc
import ua.gov.diia.ui_base.components.molecule.doc.DocNumberCopyWhiteMlcData
import ua.gov.diia.ui_base.components.molecule.doc.toUIModel
import ua.gov.diia.ui_base.components.molecule.doc.toUiModel
import ua.gov.diia.ui_base.components.molecule.text.HeadingWithSubtitlesMlc
import ua.gov.diia.ui_base.components.molecule.text.HeadingWithSubtitlesMlcData
import ua.gov.diia.ui_base.components.molecule.text.HeadingWithSubtitlesWhiteMlc
import ua.gov.diia.ui_base.components.molecule.text.HeadingWithSubtitlesWhiteMlcData
import ua.gov.diia.ui_base.components.molecule.text.toUIModel
import ua.gov.diia.ui_base.util.toUiModel

@Composable
fun DocHeadingOrg(
    modifier: Modifier = Modifier,
    data: DocHeadingOrgData,
    onUIAction: (UIAction) -> Unit
) {
    Row(modifier = modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp)
        .semantics {
            testTag = data.componentId
        }) {
        Column(modifier = Modifier.weight(1f)) {
            data.heading?.let { HeadingWithSubtitlesMlc(data = it) }
            data.headingWhite?.let { HeadingWithSubtitlesWhiteMlc(data = it) }
            data.docNumber?.let { DocNumberCopyMlc(data = it, onUIAction = onUIAction) }
            data.docNumberCopyWhite?.let {
                DocNumberCopyWhiteMlc(
                    data = it,
                    onUIAction = onUIAction
                )
            }

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

fun DocHeadingOrg?.toUiModel(): DocHeadingOrgData? {
    if (this == null) return null
    return DocHeadingOrgData(
        componentId = this.componentId.orEmpty(),
        heading = this.headingWithSubtitlesMlc?.toUiModel(),
        docNumber = this.docNumberCopyMlc?.toUiModel(),
        headingWhite = this.headingWithSubtitleWhiteMlc?.toUIModel(),
        docNumberCopyWhite = this.docNumberCopyWhiteMlc?.toUIModel()
    )
}

fun generateDocHeadingOrgMockData(): DocHeadingOrgData {
    return DocHeadingOrgData(
        id = "123",
        heading = HeadingWithSubtitlesMlcData(
            value = "Паспорт громадянина\nУкраїни\u2028",
            subtitles = listOf("Закорднонний паспорт", "Ukraine • Україна")
        ),
        docNumber = DocNumberCopyMlcData(
            value = "1234567890".toDynamicString(),
            icon = IconAtmData(
                code = DiiaResourceIcon.COPY.code
            )
        ),
    )
}

@Composable
@Preview
fun DocHeadingOrgPreview() {
    DocHeadingOrg(data = generateDocHeadingOrgMockData()) {}
}
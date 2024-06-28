package ua.gov.diia.ui_base.components.organism.document

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.ui_base.components.DiiaResourceIcon
import ua.gov.diia.ui_base.components.atom.icon.IconAtm
import ua.gov.diia.ui_base.components.atom.icon.IconAtmData
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.toDynamicString
import ua.gov.diia.ui_base.components.molecule.doc.DocNumberCopyMlc
import ua.gov.diia.ui_base.components.molecule.doc.DocNumberCopyMlcData
import ua.gov.diia.ui_base.components.molecule.doc.DocNumberCopyWhiteMlc
import ua.gov.diia.ui_base.components.molecule.doc.DocNumberCopyWhiteMlcData
import ua.gov.diia.ui_base.components.molecule.doc.StackMlc
import ua.gov.diia.ui_base.components.molecule.doc.StackMlcData
import ua.gov.diia.ui_base.components.molecule.text.HeadingWithSubtitlesMlc
import ua.gov.diia.ui_base.components.molecule.text.HeadingWithSubtitlesMlcData
import ua.gov.diia.ui_base.components.molecule.text.HeadingWithSubtitlesWhiteMlc
import ua.gov.diia.ui_base.components.molecule.text.HeadingWithSubtitlesWhiteMlcData

@Composable
fun DocButtonHeadingOrg(
    modifier: Modifier,
    data: DocButtonHeadingOrgData,
    onUIAction: (UIAction) -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .semantics {
                testTag = data.componentId
            },
        verticalAlignment = Alignment.Bottom
    ) {
        data.heading?.let {
            HeadingWithSubtitlesMlc(
                modifier = Modifier.weight(1f),
                data = it
            )
        }
        data.headingWhite?.let {
            HeadingWithSubtitlesWhiteMlc(
                modifier = Modifier.weight(1f),
                data = it
            )
        }

        data.docNumberCopy?.let {
            DocNumberCopyMlc(
                modifier = Modifier.weight(1f),
                data = it,
                onUIAction = onUIAction
            )
        }

        data.docNumberCopyWhite?.let {
            DocNumberCopyWhiteMlc(
                modifier = Modifier.weight(1f),
                data = it,
                onUIAction = onUIAction
            )
        }

        data.stackMlcData?.let {
            if (data.isStack && data.size != 1) {
                StackMlc(
                    modifier = Modifier.padding(start = 8.dp),
                    data = it,
                    onUIAction = { onUIAction(UIAction(actionKey = UIActionKeysCompose.DOC_STACK)) }
                )
            }
        }

        data.iconAtmData?.let {
            if (!data.isStack) {
                IconAtm(
                    modifier = Modifier.padding(start = 8.dp),
                    data = it,
                    onUIAction = { onUIAction(UIAction(actionKey = UIActionKeysCompose.DOC_ELLIPSE_MENU)) }
                )
            }
        }
    }
}

data class DocButtonHeadingOrgData(
    val id: String? = null,
    val componentId: String = "",
    val actionKey: String = UIActionKeysCompose.DOC_BUTTON_HEADING_DOC,
    val heading: HeadingWithSubtitlesMlcData? = null,
    val headingWhite: HeadingWithSubtitlesWhiteMlcData? = null,
    val size: Int? = null,
    val docNumberCopy: DocNumberCopyMlcData? = null,
    val docNumberCopyWhite: DocNumberCopyWhiteMlcData? = null,
    val iconAtmData: IconAtmData? = null,
    val stackMlcData: StackMlcData? = null,
    val isStack: Boolean = false
) : UIElementData

@Composable
@Preview
fun DocButtonHeadingOrgPreview() {
    val data = DocButtonHeadingOrgData(
        id = "1",
        heading = HeadingWithSubtitlesMlcData(
            value = "Name\u2028Second Name\u2028Family Name",
            subtitles = null
        ),
        isStack = false
    )
    DocButtonHeadingOrg(modifier = Modifier, data = data) {}
}

@Composable
@Preview
fun DocButtonHeadingOrgPreviewDocCopy() {
    val data = DocButtonHeadingOrgData(
        id = "1",
        docNumberCopy = DocNumberCopyMlcData(
            id = "123",
            value = "1234567890".toDynamicString(),
            icon = IconAtmData(
                code = DiiaResourceIcon.COPY.code
            )
        ),
        isStack = false
    )
    DocButtonHeadingOrg(modifier = Modifier, data = data) {}
}

@Composable
@Preview
fun DocButtonHeadingOrgWithStackPreview() {
    val data = DocButtonHeadingOrgData(
        id = "1",
        heading = HeadingWithSubtitlesMlcData(
            value = "Name\u2028Second Name\u2028Family Name",
            subtitles = null
        ),
        isStack = true,
        size = 2
    )
    DocButtonHeadingOrg(modifier = Modifier, data = data) {}
}
package ua.gov.diia.doc_driver_license

import androidx.compose.runtime.snapshots.SnapshotStateList
import ua.gov.diia.documents.barcode.DocumentBarcodeResultLoading
import ua.gov.diia.documents.models.DiiaDocument
import ua.gov.diia.documents.models.LocalizationType
import ua.gov.diia.documents.ui.DocumentComposeMapper
import ua.gov.diia.documents.ui.fullinfo.BaseFullInfoComposeMapper
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.addAllIfNotNull

class DriverLicenseFullInfoComposeMapper(
    docComposeMapper: DocumentComposeMapper
): BaseFullInfoComposeMapper, DocumentComposeMapper by docComposeMapper  {
    override fun mapDocToBody(document: DiiaDocument, bodyData: SnapshotStateList<UIElementData>) {
        if(document is DriverLicenseV2.Data) {
            bodyData.addAllIfNotNull(
                document.fullInfo?.find { it.docHeadingOrg != null }?.docHeadingOrg.toComposeDocHeadingOrg(),
                document.fullInfo?.find { it.tickerAtm != null }?.tickerAtm.toComposeTickerAtm(),
                toComposeContentTableOrg(
                    document.fullInfo?.mapNotNull { it.tableBlockTwoColumnsOrg },
                    document.fullInfo?.mapNotNull { it.tableBlockOrg },
                    document.photo?.image,
                    null
                ),
                toComposeDocCodeOrg(
                    DocumentBarcodeResultLoading(loading = true),
                    localizationType = LocalizationType.ua,
                    showToggle = true
                )
            )
        }
    }
}
package ua.gov.diia.doc_driver_license.utils

import androidx.compose.runtime.snapshots.SnapshotStateList
import ua.gov.diia.doc_driver_license.models.v2.DriverLicenseV2
import ua.gov.diia.core.models.document.DiiaDocument
import ua.gov.diia.core.models.document.LocalizationType
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.addAllIfNotNull
import ua.gov.diia.ui_base.components.organism.document.Localization
import ua.gov.diia.ui_base.components.organism.document.toUIModel
import ua.gov.diia.ui_base.mappers.document.BaseFullInfoComposeMapper
import ua.gov.diia.ui_base.mappers.document.DocumentComposeMapper

class DriverLicenseFullInfoComposeMapper(
    docComposeMapper: DocumentComposeMapper
) : BaseFullInfoComposeMapper, DocumentComposeMapper by docComposeMapper {
    override fun mapDocToBody(document: DiiaDocument, bodyData: SnapshotStateList<UIElementData>) {
        if (document is DriverLicenseV2.Data) {
            bodyData.addAllIfNotNull(
                document.fullInfo?.find { it.docHeadingOrg != null }?.docHeadingOrg.toComposeDocHeadingOrg(),
                document.fullInfo?.find { it.tickerAtm != null }?.tickerAtm.toComposeTickerAtm(),
                toComposeContentTableOrg(
                    document.fullInfo?.mapNotNull { it.tableBlockTwoColumnsOrg },
                    document.fullInfo?.mapNotNull { it.tableBlockOrg },
                    null,
                    document.photo?.image,
                    null
                ),
                document.fullInfo?.find { it.verificationCodesOrg != null }?.verificationCodesOrg?.toUIModel(
                    localization = when (document.localization()) {
                        LocalizationType.ua -> Localization.ua
                        LocalizationType.eng -> Localization.eng
                        null -> Localization.ua
                    }
                )
            )
        }
    }
}
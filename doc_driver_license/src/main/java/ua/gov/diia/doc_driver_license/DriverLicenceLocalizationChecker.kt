package ua.gov.diia.doc_driver_license

import ua.gov.diia.documents.models.DiiaDocumentWithMetadata
import ua.gov.diia.documents.ui.BaseLocalizationChecker

class DriverLicenceLocalizationChecker: BaseLocalizationChecker {
    override fun checkLocalizationDocs(
        doc: DiiaDocumentWithMetadata
    ): String? {
        if (doc.diiaDocument is DriverLicenseV2.Data) {
            val document = doc.diiaDocument as DriverLicenseV2.Data
            if (document.frontCard.ua == null) {
                return DriverLicenseConst.NAME
            }
        }
        return null
    }
}
package ua.gov.diia.doc_driver_license.utils

import ua.gov.diia.core.models.document.BaseLocalizationChecker
import ua.gov.diia.doc_driver_license.models.DocName
import ua.gov.diia.doc_driver_license.models.v2.DriverLicenseV2
import ua.gov.diia.core.models.document.DiiaDocumentWithMetadata

class DriverLicenceLocalizationChecker: BaseLocalizationChecker {
    override fun checkLocalizationDocs(
        doc: DiiaDocumentWithMetadata
    ): String? {
        if (doc.diiaDocument is DriverLicenseV2.Data) {
            val document = doc.diiaDocument as DriverLicenseV2.Data
            if (document.frontCard.ua == null) {
                return DocName.DRIVER_LICENSE
            }
        }
        return null
    }
}
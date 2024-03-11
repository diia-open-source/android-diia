package ua.gov.diia.opensource.helper.documents

import ua.gov.diia.doc_driver_license.DriverLicenseV2
import ua.gov.diia.documents.models.DiiaDocument
import ua.gov.diia.documents.util.DocNameProvider
import javax.inject.Inject

class DocNameProviderImpl @Inject constructor() : DocNameProvider {
    override fun getDocumentName(document: DiiaDocument): String {
        return when (document) {
            is DriverLicenseV2.Data -> document.getItemType()
            else -> ""
        }
    }
}
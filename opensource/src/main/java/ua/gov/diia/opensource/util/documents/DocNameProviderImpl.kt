package ua.gov.diia.opensource.util.documents

import ua.gov.diia.core.models.document.DiiaDocument
import ua.gov.diia.documents.util.DocNameProvider
import javax.inject.Inject

class DocNameProviderImpl @Inject constructor() : DocNameProvider {
    override fun getDocumentName(document: DiiaDocument): String {
        return document.getItemType()
    }
}
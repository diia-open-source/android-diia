package ua.gov.diia.documents.util

import ua.gov.diia.documents.data.repository.DocumentsDataRepository
import ua.gov.diia.documents.helper.DocumentsHelper
import javax.inject.Inject

class WithUpdateExpiredDocsImpl @Inject constructor(private val documentsDataSource: DocumentsDataRepository,
    private val documentsHelper: DocumentsHelper) :
    WithUpdateExpiredDocs {
    override suspend fun updateExpirationDate(focusDocType: String) : Boolean {
        documentsHelper.provideListOfDocumentsRequireUpdateOfExpirationDate(focusDocType)?.let {
            updateExpirationDate(it)
            return true
        }
        return false
    }

    override suspend fun updateExpirationDate(types: List<String>) {
        documentsDataSource.replaceExpDateByType(types)
        documentsDataSource.invalidate()
    }
}
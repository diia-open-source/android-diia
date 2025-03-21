package ua.gov.diia.documents.data.datasource.remote

import ua.gov.diia.core.util.delegation.WithCrashlytics
import ua.gov.diia.diia_storage.store.datasource.DataSourceDataResult
import ua.gov.diia.documents.data.api.ApiDocuments
import ua.gov.diia.core.models.document.DiiaDocumentWithMetadata
import ua.gov.diia.documents.models.DocumentsOrder
import ua.gov.diia.documents.models.FetchDocumentsResult
import ua.gov.diia.documents.models.TypeDefinedDocumentsOrder

class NetworkDocumentsDataSource (
    private val apiDocs: ApiDocuments,
    private val withCrashlytics: WithCrashlytics
) : WithCrashlytics by withCrashlytics {

    suspend fun fetchData(docTypes: Set<String>): DataSourceDataResult<List<DiiaDocumentWithMetadata>> {
        return try {
            DataSourceDataResult.successful(apiDocs.fetchDocuments(docTypes.mapTypes()))
        } catch (e: Exception) {
            sendNonFatalError(e)
            DataSourceDataResult.failed(e)
        }
    }

    /**
     * Returns pair of requested documents and general documents order
     */
    suspend fun fetchDocsWithTypes(docTypes: Set<String>): FetchDocumentsResult {
        return try {
            val data = apiDocs.fetchDocumentsWithTypes(docTypes.mapTypes())
            FetchDocumentsResult(
                documents = data.documents,
                docOrder = data.docOrder
            )
        } catch (e: Exception) {
            FetchDocumentsResult(exception = e)
        }
    }

    suspend fun saveDocOrderForSpecificType(
        documentType: String,
        docOrder: TypeDefinedDocumentsOrder
    ) {
        try {
            apiDocs.setTypedDocumentsOrder(
                documentType = documentType,
                docOrder = docOrder
            )
        } catch (e: Exception) {
            sendNonFatalError(e)
        }
    }

    suspend fun setDocumentsOrder(docOrder: DocumentsOrder) {
        try {
            apiDocs.setDocumentsOrder(docOrder = docOrder)
        } catch (e: Exception) {
            sendNonFatalError(e)
        }
    }

    private fun Collection<String>.mapTypes(): Map<String, String> {
        return withIndex().associateBy({ "filter[${it.index}]" }, { it.value })
    }

}
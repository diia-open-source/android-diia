package ua.gov.diia.documents.data.repository

import ua.gov.diia.diia_storage.store.datasource.DataSource
import ua.gov.diia.core.models.document.DiiaDocument
import ua.gov.diia.core.models.document.DiiaDocumentWithMetadata
import ua.gov.diia.documents.models.DocOrder
import ua.gov.diia.documents.models.TypeDefinedDocOrder

interface DocumentsDataRepository : DataSource<List<DiiaDocumentWithMetadata>> {

    fun updateDocOrder(docTypeList: List<String>)

    fun attachExternalDocument(document: DiiaDocumentWithMetadata)

    fun removeDocument(diiaDocument: DiiaDocument)

    fun removeDocumentByType(type: String)

    fun updateDocument(diiaDocument: DiiaDocument)

    fun replaceExpDateByType(types: List<String>)

    fun saveDocTypeOrder(docOrders: List<DocOrder>)

    fun saveDocOrderForSpecificType(
        docOrders: List<TypeDefinedDocOrder>,
        docType: String
    )

    suspend fun getDocsByType(type: String): List<DiiaDocument?>? //

    suspend fun getETagForDocType(type: String): String

    suspend fun loadLocalDocData(): List<DiiaDocumentWithMetadata>?

    suspend fun saveDocsToStorage(
        data: List<DiiaDocumentWithMetadata>
    )

    suspend fun transformDocumentToEngagedCert(type: String, dateIssued: String)

    suspend fun clear()

}

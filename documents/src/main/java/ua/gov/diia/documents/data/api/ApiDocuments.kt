package ua.gov.diia.documents.data.api

import ua.gov.diia.documents.models.*

interface ApiDocuments {

    /**
     * @return full list of documents
     */
    suspend fun fetchDocuments(docTypes: Map<String, String>): List<DiiaDocumentWithMetadata>
    /**
     * @return list of documents with specific types
     */
    suspend fun fetchDocumentsWithTypes(docTypes: Map<String, String>): DiiaDocumentsWithOrder
    /**
     * Set new document order
     */
    suspend fun setDocumentsOrder(docOrder: DocumentsOrder)
    /**
     * Set new typed document order
     */
    suspend fun setTypedDocumentsOrder(documentType: String, docOrder: TypeDefinedDocumentsOrder)
    /**
     * @return list of docs to add manually
     */
    suspend fun getDocsManual(): ManualDocs
    /**
     * @return document by id
     */
    suspend fun getDocumentById(type: String, id: String): UpdatedDoc
}
package ua.gov.diia.documents.data.datasource.local

import ua.gov.diia.documents.models.DiiaDocument
import ua.gov.diia.documents.models.DiiaDocumentWithMetadata

interface RemoveExpiredDocBehavior {
    /**
     * Removing expired documents
     */
    suspend fun removeExpiredDocs(
        data: List<DiiaDocumentWithMetadata>,
        remove: suspend (DiiaDocument) -> Unit
    )
}
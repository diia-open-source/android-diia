package ua.gov.diia.documents.data.datasource.local

import ua.gov.diia.core.models.document.DiiaDocument
import ua.gov.diia.core.models.document.DiiaDocumentWithMetadata

interface RemoveExpiredDocBehavior {
    /**
     * Removing expired documents
     */
    suspend fun removeExpiredDocs(
        data: List<DiiaDocumentWithMetadata>,
        remove: suspend (DiiaDocument) -> Unit
    )
}
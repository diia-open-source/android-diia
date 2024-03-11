package ua.gov.diia.documents.data.repository

import ua.gov.diia.documents.models.DiiaDocumentWithMetadata

interface BeforePublishAction {
    /**
     * perform some action before publish documents
     */
    suspend fun perform(data: List<DiiaDocumentWithMetadata>)
}
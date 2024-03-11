package ua.gov.diia.documents.data.datasource.local

import ua.gov.diia.documents.models.DiiaDocumentWithMetadata

interface DocumentsTransformation {

    /**
     * Transforms documents state
     */
    fun transform(data: List<DiiaDocumentWithMetadata>)
}
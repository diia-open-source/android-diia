package ua.gov.diia.documents.data.datasource.local

import ua.gov.diia.core.models.document.DiiaDocumentWithMetadata

interface DocumentsTransformation {

    /**
     * Transforms documents state
     */
    fun transform(data: List<DiiaDocumentWithMetadata>)
}
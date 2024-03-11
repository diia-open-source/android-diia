package ua.gov.diia.documents.data.datasource.local

import ua.gov.diia.documents.models.DiiaDocumentWithMetadata

interface BrokenDocFilter {
    /**
     * Filters existing documents and fills list of documents to remove
     */
    fun filter(
        docs: List<DiiaDocumentWithMetadata>,
        existsId: MutableList<String?>,
        removeList: MutableList<DiiaDocumentWithMetadata>
    )
}
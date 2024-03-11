package ua.gov.diia.documents.data.datasource.local

import ua.gov.diia.documents.models.DiiaDocumentWithMetadata

interface DocGroupUpdateBehavior {

    /**
     * @return true if implementation can handle this type of document
     */
    fun canHandleType(type: String): Boolean

    /**
     * Handle update of existing document group
     */
    fun handleUpdate(
        docType: String,
        docValue: List<DiiaDocumentWithMetadata>,
        status: Int,
        docsToPersist: MutableList<DiiaDocumentWithMetadata>,
        existsId: List<String?>
    )
}
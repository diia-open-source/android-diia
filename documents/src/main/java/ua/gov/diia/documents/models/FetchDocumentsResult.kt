package ua.gov.diia.documents.models

import ua.gov.diia.core.models.document.DiiaDocumentWithMetadata

data class FetchDocumentsResult(
    val documents: List<DiiaDocumentWithMetadata> = emptyList(),
    val docOrder: List<String> = emptyList(),
    var exception: Exception? = null,
    val isSuccessful: Boolean = exception == null
)
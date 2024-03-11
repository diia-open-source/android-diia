package ua.gov.diia.documents.models

data class FetchDocumentsResult(
    val documents: List<DiiaDocumentWithMetadata> = emptyList(),
    val docOrder: List<String> = emptyList(),
    var exception: Exception? = null,
    val isSuccessful: Boolean = exception == null
)
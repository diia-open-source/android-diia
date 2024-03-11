package ua.gov.diia.documents.models

data class DiiaDocumentsWithOrder(
    val documents: List<DiiaDocumentWithMetadata> = emptyList(),
    val docOrder: List<String> = emptyList()
)
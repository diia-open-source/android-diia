package ua.gov.diia.documents.models

import ua.gov.diia.core.models.document.DiiaDocumentWithMetadata
data class DiiaDocumentsWithOrder(
    val documents: List<DiiaDocumentWithMetadata> = emptyList(),
    val docOrder: List<String> = emptyList()
)
package ua.gov.diia.documents.barcode

import ua.gov.diia.documents.models.DiiaDocument

interface DocumentBarcodeRepository {
    suspend fun loadBarcode(
        doc: DiiaDocument,
        position: Int,
        fullInfo: Boolean = false
    ): DocumentBarcodeRepositoryResult
}

data class DocumentBarcodeRepositoryResult(
    val result: DocumentBarcodeResult,
    val showToggle: Boolean
)
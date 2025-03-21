package ua.gov.diia.core.models.document.barcode

import ua.gov.diia.core.models.document.DiiaDocument

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
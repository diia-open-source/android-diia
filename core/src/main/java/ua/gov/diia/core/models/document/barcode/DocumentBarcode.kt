package ua.gov.diia.core.models.document.barcode

data class DocumentBarcode(
    val data: DocumentBarcodeImageData
)

interface DocumentBarcodeResult

data class DocumentBarcodeSuccessfulLoadResult(
    val shareQr: DocumentBarcode,
    val shareEan13: DocumentBarcode?,
    val shareEanCode: String?,
    val position: Int,
    val timerText: String?,
    val timerTime: Int?
) : DocumentBarcodeResult

data class DocumentBarcodeErrorLoadResult(val exception: Exception, val code: Int? = null) :
    DocumentBarcodeResult

data class DocumentBarcodeResultLoading(val loading: Boolean) :
    DocumentBarcodeResult
package ua.gov.diia.core.models.document.barcode

import android.graphics.Bitmap


fun Bitmap.toDocumentBarcodeImage(): DocumentBarcodeImageData {
    return DocumentBarcodeImageData(this)
}

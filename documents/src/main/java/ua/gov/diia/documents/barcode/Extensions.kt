package ua.gov.diia.documents.barcode

import android.graphics.Bitmap


fun Bitmap.toDocumentBarcodeImage(): DocumentBarcodeImageData {
    return DocumentBarcodeImageData(this)
}

package ua.gov.diia.core.models.document

import android.graphics.Bitmap

data class DocumentCard(
    var doc: DiiaDocumentWithMetadata,
    var docCount: Int = 1,
    var qrLoadException: java.lang.Exception? = null,
    var qrBitmap: Bitmap? = null,
    var ean13Bitmap: Bitmap? = null,
    var eanCode: String? = null,
)
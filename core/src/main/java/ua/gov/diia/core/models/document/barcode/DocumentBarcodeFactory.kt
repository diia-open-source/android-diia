package ua.gov.diia.core.models.document.barcode

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.util.Base64
import com.google.zxing.BarcodeFormat
import com.google.zxing.common.BitMatrix
import com.google.zxing.oned.EAN13Writer
import com.google.zxing.qrcode.QRCodeWriter
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel
import ua.gov.diia.core.util.extensions.image_processing.replaceWhiteWithTransparent
import java.util.stream.IntStream
import kotlin.math.roundToInt

class DocumentBarcodeFactory(
    private val qrSizePx: Int,
    private val ean13CodeWidth: Int,
    private val ean13CodeHeight: Int
) {

    private var qrBmp: Bitmap? = null
    private var eanBmp: Bitmap? = null

    suspend fun buildBitmapQrCode(data: String) {
        val code = QRCodeWriter().encode(
            data,
            BarcodeFormat.QR_CODE,
            qrSizePx,
            qrSizePx,
            mapOf(
                com.google.zxing.EncodeHintType.MARGIN to 0,
                com.google.zxing.EncodeHintType.ERROR_CORRECTION to ErrorCorrectionLevel.H
            )
        )
        this.qrBmp = qrToBitmap(code, qrSizePx)
    }

    suspend fun buildBitmapEan13Code(data: String) {
        val ean = EAN13Writer().encode(data)
        this.eanBmp = ean13ToBitmap(ean, ean13CodeWidth, ean13CodeHeight)
    }

    fun getQrCodeResult(): DocumentBarcode {
        val qrBmp = this.qrBmp
        checkNotNull(qrBmp)
        return DocumentBarcode(qrBmp.toDocumentBarcodeImage())
    }

    fun getEan13CodeResult(): DocumentBarcode? {
        val eanBmp = this.eanBmp ?: return null
        return DocumentBarcode(eanBmp.toDocumentBarcodeImage())
    }

    suspend fun awaitBitmapQrCode(data: String): DocumentBarcodeImageData {
        buildBitmapQrCode(data)
        return (getQrCodeResult()).also { clearResults() }.data
    }

    fun parseBase64Barcode(data: String): DocumentBarcode {
        val imageBytes = Base64.decode(data, Base64.DEFAULT)
        val decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
            .replaceWhiteWithTransparent()
        return DocumentBarcode(decodedImage.toDocumentBarcodeImage())
    }

    fun clearResults() {
        eanBmp = null
        qrBmp = null
    }

    private fun ean13ToBitmap(code: BooleanArray, width: Int, height: Int): Bitmap {
        val inputWidth = code.size
        val outputWidth = inputWidth * (width.toFloat() / inputWidth.toFloat()).roundToInt()
        val multiple = outputWidth / inputWidth
        var inputX = 0
        var outputX = 0
        val bmp = Bitmap.createBitmap(outputWidth, height, Bitmap.Config.ARGB_8888)
        while (inputX < inputWidth) {
            if (code[inputX]) {
                (0 until height).forEach { y ->
                    for (columnIndex in outputX until outputX + multiple) {
                        bmp.setPixel(columnIndex, y, Color.BLACK)
                    }
                }
            }
            inputX++
            outputX += multiple
        }
        return bmp
    }

    private fun qrToBitmap(code: BitMatrix, size: Int): Bitmap {
        return Bitmap.createBitmap(
            IntStream.range(0, size).flatMap { h ->
                IntStream.range(0, size).map { w ->
                    if (code.get(w, h)) Color.BLACK else Color.TRANSPARENT
                }
            }.parallel().toArray(),
            size, size, Bitmap.Config.ARGB_8888
        )
    }

    suspend fun fetchBitmap(code: String, function: (Bitmap) -> Unit) {
        buildBitmapQrCode(code)
        function(getQrCodeResult().data.toAndroidBitmap())
        clearResults()
    }
}
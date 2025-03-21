package ua.gov.diia.core.models.document.barcode

import android.graphics.*
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.common.internal.ImageConvertUtils

class DocumentBarcodeImageData(bitmap: Bitmap) {

    private val inputImage: InputImage

    private val width: Int
    private val height: Int

    init {
        this.inputImage = InputImage.fromBitmap(bitmap, 0)
        this.width = bitmap.width
        this.height = bitmap.height
    }

    fun toAndroidBitmap(): Bitmap {
        return inputImage.bitmapInternal ?: ImageConvertUtils.getInstance()
            .getUpRightBitmap(this.toFirebaseMlInputImage())
    }

    private fun toFirebaseMlInputImage(): InputImage = this.inputImage

}
package ua.gov.diia.core.util.extensions.image_processing

import android.graphics.Bitmap
import android.graphics.Color
import androidx.core.graphics.set
import kotlin.math.abs

fun Bitmap.replaceWhiteWithTransparent(): Bitmap {
    val bitmap = copy(Bitmap.Config.ARGB_8888, true)
    val width = bitmap.width
    val height = bitmap.height
    var leftMostPixelIndex = Integer.MAX_VALUE

    fun pixelIsBlack(color: Int): Boolean {
        val blackThreshold = 128

        val br = Color.red(Color.BLACK)
        val bg = Color.green(Color.BLACK)
        val bb = Color.blue(Color.BLACK)

        val r = Color.red(color)
        val g = Color.green(color)
        val b = Color.blue(color)


        if ((abs(br - r) < blackThreshold) && (abs(bg - g) < blackThreshold) && (abs(bb - b) < blackThreshold)) {
            return true
        }
        return false
    }

    (0 until width).forEach { x ->
        (0 until height).forEach { y ->
            val currentPixel = bitmap.getPixel(x, y)
            if (!pixelIsBlack(currentPixel)) {
                bitmap[x, y] = Color.TRANSPARENT
            } else {
                if (leftMostPixelIndex > x) {
                    leftMostPixelIndex = x
                }
            }
        }
    }
    return Bitmap.createBitmap(
        bitmap,
        leftMostPixelIndex,
        0,
        if (width - leftMostPixelIndex <= 0) 1 else width - leftMostPixelIndex,
        height
    ).apply {
        setHasAlpha(true)
    }
}
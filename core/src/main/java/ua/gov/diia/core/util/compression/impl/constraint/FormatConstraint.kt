package ua.gov.diia.core.util.compression.impl.constraint

import android.graphics.Bitmap
import ua.gov.diia.core.util.compression.impl.compressFormat
import ua.gov.diia.core.util.compression.impl.loadBitmap
import ua.gov.diia.core.util.compression.impl.overWrite
import java.io.File

class FormatConstraint(private val format: Bitmap.CompressFormat) : Constraint {

    override fun isSatisfied(imageFile: File): Boolean {
        return format == imageFile.compressFormat()
    }

    override fun satisfy(imageFile: File): File {
        return overWrite(imageFile, loadBitmap(imageFile), format)
    }
}

fun Compression.format(format: Bitmap.CompressFormat) {
    constraint(FormatConstraint(format))
}
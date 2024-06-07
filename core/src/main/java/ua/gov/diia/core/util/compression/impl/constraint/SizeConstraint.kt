package ua.gov.diia.core.util.compression.impl.constraint

import android.util.Base64
import ua.gov.diia.core.util.compression.impl.loadBitmap
import ua.gov.diia.core.util.compression.impl.overWrite
import java.io.File
import java.io.FileInputStream

class SizeConstraint(
    private val maxFileSize: Long,
    private val stepSize: Int = 10,
    private val maxIteration: Int = 10,
    private val minQuality: Int = 5,
    private val verifyForBase64: Boolean,
) : Constraint {
    private var iteration: Int = 0

    override fun isSatisfied(imageFile: File): Boolean {
        val satisfySize = imageFile.length() <= maxFileSize || iteration >= maxIteration
        return if (verifyForBase64) {
            val b64Image = getBase64EncodedFile(imageFile) ?: return false
            return b64Image.size <= maxFileSize || iteration >= maxIteration
        } else {
            satisfySize
        }
    }

    override fun satisfy(imageFile: File): File {
        iteration++
        val quality = (100 - iteration * stepSize).takeIf { it >= minQuality } ?: minQuality
        return overWrite(imageFile, loadBitmap(imageFile), quality = quality)
    }

    private fun getBase64EncodedFile(file: File): ByteArray? {
        var inStream: FileInputStream? = null
        try {
            inStream = FileInputStream(file)
            val imageBytes = ByteArray(file.length().toInt())
            inStream.read(imageBytes, 0, imageBytes.size)
            return Base64.encode(imageBytes, Base64.DEFAULT)
        } finally {
            inStream?.close()
        }
    }
}

fun Compression.size(
    maxFileSize: Long,
    stepSize: Int = 10,
    maxIteration: Int = 10,
    verifyForBase64: Boolean = false
) {
    constraint(SizeConstraint(maxFileSize, stepSize, maxIteration, verifyForBase64 = verifyForBase64))
}
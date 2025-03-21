package ua.gov.diia.core.util.extensions.image_processing

import android.util.Base64
import java.io.File
import java.io.FileInputStream

fun File.imageFileToBase64(): String? {
    var inStream: FileInputStream? = null
    try {
        inStream = FileInputStream(this)
        val imageBytes = ByteArray(this.length().toInt())
        inStream.read(imageBytes, 0, imageBytes.size)

        return Base64.encodeToString(imageBytes, Base64.DEFAULT)
    } finally {
        inStream?.close()
    }
}
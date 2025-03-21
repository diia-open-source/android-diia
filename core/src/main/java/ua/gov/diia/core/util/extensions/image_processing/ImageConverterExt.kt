package ua.gov.diia.core.util.extensions.image_processing

import androidx.core.net.toUri
import ua.gov.diia.core.util.compression.DiiaFileCompressor

suspend fun DiiaFileCompressor.getCompressedImage(
    imgUri: String,
    preferredSize: Long
): String? = try {
    compressPicture(
        imgUri.toUri(),
        preferredSize,
        true
    ).imageFileToBase64()
} catch (e: Exception) {
    null
}

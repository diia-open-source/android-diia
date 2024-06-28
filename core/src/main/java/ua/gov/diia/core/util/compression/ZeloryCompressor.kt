package ua.gov.diia.core.util.compression

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import dagger.hilt.android.qualifiers.ApplicationContext
import ua.gov.diia.core.util.compression.impl.Compressor
import ua.gov.diia.core.util.compression.impl.constraint.format
import ua.gov.diia.core.util.compression.impl.constraint.resolution
import ua.gov.diia.core.util.compression.impl.constraint.size
import java.io.File
import javax.inject.Inject

class ZeloryCompressor @Inject constructor(
    @ApplicationContext private val context: Context
) : DiiaFileCompressor {

    override suspend fun compressPicture(
        pictureUri: Uri,
        maxSize: Long,
        verifyForBase64: Boolean
    ): File {
        return Compressor.compress(context, pictureUri) {
            resolution(PREFERRED_IMAGE_WIDTH, PREFERRED_IMAGE_HEIGHT)
            format(Bitmap.CompressFormat.JPEG)
            size(
                maxFileSize = maxSize,
                maxIteration = Int.MAX_VALUE,
                verifyForBase64 = verifyForBase64
            )
        }
    }

    private companion object {
        const val PREFERRED_IMAGE_WIDTH = 1024
        const val PREFERRED_IMAGE_HEIGHT = 768
    }
}
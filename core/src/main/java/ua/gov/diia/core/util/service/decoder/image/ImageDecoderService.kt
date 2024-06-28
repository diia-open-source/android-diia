package ua.gov.diia.core.util.service.decoder.image

import android.graphics.Bitmap
import android.net.Uri

interface ImageDecoderService {

    suspend fun decode(
        imgUri: Uri,
        format: Bitmap.CompressFormat,
        quality: Int
    ): Uri
}
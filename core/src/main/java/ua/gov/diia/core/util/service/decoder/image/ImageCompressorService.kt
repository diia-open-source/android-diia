package ua.gov.diia.core.util.service.decoder.image

import android.net.Uri

interface ImageCompressorService {

    suspend fun decode(
        imgUri: Uri,
        maxFileSize: Long?
    ): Uri
}
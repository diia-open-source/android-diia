package ua.gov.diia.core.util.service.decoder.video

import android.net.Uri

interface VideoDecoderService {

    suspend fun decode(uri: Uri): Uri?
}
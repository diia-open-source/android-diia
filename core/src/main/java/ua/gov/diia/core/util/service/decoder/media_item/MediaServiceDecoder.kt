package ua.gov.diia.core.util.service.decoder.media_item

import android.net.Uri
import ua.gov.diia.core.models.media.MediaItem

interface MediaServiceDecoder {

    suspend fun decode(item: MediaItem, maxFileSize: Long?): Uri?
}
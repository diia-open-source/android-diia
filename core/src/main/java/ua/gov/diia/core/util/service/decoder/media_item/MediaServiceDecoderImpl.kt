package ua.gov.diia.core.util.service.decoder.media_item

import android.net.Uri
import ua.gov.diia.core.models.media.MediaFileType
import ua.gov.diia.core.models.media.MediaItem
import ua.gov.diia.core.util.service.decoder.image.ImageCompressorService
import ua.gov.diia.core.util.service.decoder.image.ImageDecoderService
import ua.gov.diia.core.util.service.decoder.video.VideoDecoderService
import javax.inject.Inject

class MediaServiceDecoderImpl @Inject constructor(
    private val imageCompressor: ImageCompressorService,
    private val imageDecoder: ImageDecoderService,
    private val videoDecoder: VideoDecoderService
) : MediaServiceDecoder {

    override suspend fun decode(item: MediaItem, maxFileSize: Long?): Uri? = when (item.type) {
        MediaFileType.IMAGE -> imageCompressor.decode(imgUri = item.uri, maxFileSize)

        MediaFileType.VIDEO -> videoDecoder.decode(item.uri)
    }
}
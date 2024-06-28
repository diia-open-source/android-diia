package ua.gov.diia.core.di.services

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import ua.gov.diia.core.util.service.decoder.image.ImageCompressorService
import ua.gov.diia.core.util.service.decoder.image.ImageCompressorServiceImpl
import ua.gov.diia.core.util.service.decoder.image.ImageDecoderService
import ua.gov.diia.core.util.service.decoder.image.ImageDecoderServiceImpl
import ua.gov.diia.core.util.service.decoder.media_item.MediaServiceDecoder
import ua.gov.diia.core.util.service.decoder.media_item.MediaServiceDecoderImpl
import ua.gov.diia.core.util.service.decoder.video.VideoDecoderService
import ua.gov.diia.core.util.service.decoder.video.VideoDecoderServiceImpl
import ua.gov.diia.core.util.service.exif.ExifAttrService
import ua.gov.diia.core.util.service.exif.ExifAttrServiceImpl

@Module
@InstallIn(ViewModelComponent::class)
interface ViewModelServicesModule {

    @Binds
    fun bindImageExifService(impl: ExifAttrServiceImpl): ExifAttrService

    @Binds
    fun bindImageDecoderService(impl: ImageDecoderServiceImpl): ImageDecoderService

    @Binds
    fun bindImageCompressorService(impl: ImageCompressorServiceImpl): ImageCompressorService

    @Binds
    fun bindVideoDecoderService(impl: VideoDecoderServiceImpl): VideoDecoderService

    @Binds
    fun bindMediaFileDecoder(impl: MediaServiceDecoderImpl): MediaServiceDecoder
}
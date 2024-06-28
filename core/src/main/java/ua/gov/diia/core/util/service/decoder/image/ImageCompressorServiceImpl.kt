package ua.gov.diia.core.util.service.decoder.image

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.core.net.toUri
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import ua.gov.diia.core.util.compression.impl.Compressor
import ua.gov.diia.core.util.compression.impl.constraint.format
import ua.gov.diia.core.util.compression.impl.constraint.size
import java.io.File
import java.io.FileNotFoundException
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class ImageCompressorServiceImpl @Inject constructor(
    @ApplicationContext
    private val appContext: Context,
) : ImageCompressorService {

    override suspend fun decode(
        imgUri: Uri, maxFileSize: Long?
    ): Uri = suspendCancellableCoroutine { cnt ->

        try {
            GlobalScope.launch {
                val compressedImgFile: File =
                    Compressor.compress(appContext, imgUri) {
                        format(Bitmap.CompressFormat.JPEG)
                        size(maxFileSize ?: 900000) // 0,9 Mb - Default value
                    }
                cnt.resume(compressedImgFile.toUri())
            }
        } catch (exception: FileNotFoundException) {
            cnt.resumeWithException(exception)
        }
    }
}
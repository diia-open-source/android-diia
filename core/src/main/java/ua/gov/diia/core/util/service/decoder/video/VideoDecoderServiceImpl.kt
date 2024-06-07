package ua.gov.diia.core.util.service.decoder.video

import android.content.Context
import android.net.Uri
import android.os.Environment
import com.abedelazizshe.lightcompressorlibrary.CompressionListener
import com.abedelazizshe.lightcompressorlibrary.VideoCompressor
import com.abedelazizshe.lightcompressorlibrary.VideoQuality
import com.abedelazizshe.lightcompressorlibrary.config.Configuration
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.suspendCancellableCoroutine
import java.io.File
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class VideoDecoderServiceImpl @Inject constructor(
    @ApplicationContext private val appContext: Context
) : VideoDecoderService {

    override suspend fun decode(uri: Uri): Uri? = suspendCancellableCoroutine { cont ->

        try {
            val resultListener = object : CompressionListener {
                override fun onCancelled(index: Int) {
                    cont.resume(null)
                }

                override fun onFailure(index: Int, failureMessage: String) {
                    cont.resume(null)
                }

                override fun onProgress(index: Int, percent: Float) {

                }

                override fun onStart(index: Int) {

                }

                override fun onSuccess(index: Int, size: Long, path: String?) {
                    val compressedFileUri = if (path != null) {
                        Uri.fromFile(File(path))
                    } else {
                        null
                    }

                    cont.resume(compressedFileUri)
                }
            }

            val config = Configuration(
                quality = VideoQuality.MEDIUM,
                frameRate = 15,
                isMinBitrateCheckEnabled = false
            )

            VideoCompressor.start(
                context = appContext,
                uris = listOf(uri),
                saveAt = Environment.DIRECTORY_DCIM,
                configureWith = config,
                listener = resultListener
            )

        } catch (e: Exception) {
            cont.resumeWithException(e)
        }

        cont.invokeOnCancellation {
            VideoCompressor.cancel()
        }
    }
}
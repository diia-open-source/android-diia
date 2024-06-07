package ua.gov.diia.core.util.service.decoder.image

import android.content.Context
import android.content.ContextWrapper
import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.core.net.toUri
import androidx.exifinterface.media.ExifInterface
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.suspendCancellableCoroutine
import ua.gov.diia.core.util.delegation.WithCrashlytics
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.util.UUID
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class ImageDecoderServiceImpl @Inject constructor(
    @ApplicationContext private val appContext: Context,
    private val crashlytics: WithCrashlytics,
) : ImageDecoderService {

    private companion object {
        private const val LANDSCAPE_H = 628
        private const val LANDSCAPE_W = 1024

        private const val PORTRAIT_H = 1024
        private const val PORTRAIT_W = 1080
    }

    private val imageAttr = setOf(
        ExifInterface.TAG_IMAGE_WIDTH,
        ExifInterface.TAG_IMAGE_LENGTH,
        ExifInterface.TAG_DATETIME,
        ExifInterface.TAG_EXPOSURE_TIME,
        ExifInterface.TAG_FLASH,
        ExifInterface.TAG_GPS_ALTITUDE,
        ExifInterface.TAG_GPS_ALTITUDE_REF,
        ExifInterface.TAG_GPS_DATESTAMP,
        ExifInterface.TAG_GPS_LATITUDE,
        ExifInterface.TAG_GPS_LATITUDE_REF,
        ExifInterface.TAG_GPS_LONGITUDE,
        ExifInterface.TAG_GPS_LONGITUDE_REF,
        ExifInterface.TAG_GPS_PROCESSING_METHOD,
        ExifInterface.TAG_GPS_TIMESTAMP,
        ExifInterface.TAG_MAKE,
        ExifInterface.TAG_MODEL,
        ExifInterface.TAG_ORIENTATION
    )


    /**
     * Performs image scaling to match the application landscape and portrait standards
     * and compression as per the [format] and [quality] parameters.
     */
    override suspend fun decode(
        imgUri: Uri,
        format: CompressFormat,
        quality: Int
    ): Uri = suspendCancellableCoroutine { cnt ->
        var outScream: FileOutputStream? = null
        var inStream: InputStream? = null

        val placeholderFilePath = buildTmpFilePath(format)
        val placeholderFile = File(placeholderFilePath)

        try {
            withNewBounds(imgUri) { w, h ->
                //open stream
                inStream = appContext.contentResolver.openInputStream(imgUri)
                outScream = FileOutputStream(placeholderFile)
                val originBitmap = BitmapFactory.decodeStream(inStream)

                /*
                Creates a new bitmap, scaled from an existing bitmap, when possible. If the
                specified width and height are the same as the current width and height of
                the source bitmap, the source bitmap is returned and no new bitmap is
                created
                 */
                val scaledBitmap = Bitmap.createScaledBitmap(originBitmap, w, h, false)
                //write new compressed and scaled bitmap to the placeholder file
                scaledBitmap.compress(format, quality, outScream!!)
                //copy metadata from the old img to the new one
                copyExif(placeholderFile, imgUri)
                //return result after compression
                cnt.resume(placeholderFile.toUri())
            }
        } catch (exception: FileNotFoundException) {
            cnt.resumeWithException(exception)
        } finally {
            inStream?.close()
            outScream?.close()
        }

        cnt.invokeOnCancellation {
            outScream?.close()
            inStream?.close()
        }
    }

    /**
     * Calculates bounds for the new decoded bitmap as per the application landscape
     * and portrait standards.
     *
     * @param invoke  callback which invokes after scaling with new width and height values.
     */
    private inline fun withNewBounds(
        imgUri: Uri,
        invoke: (w: Int, h: Int) -> Unit
    ) {
        BitmapFactory.Options().run {

            /*
             *  Prevents loading full bitmap into memory but still allows to get
             *  data from options.out fields
             */
            inJustDecodeBounds = true


            var inStream: InputStream? = null

            try {

                /*
                 * Decodes the image file provided by the provided Uri and fetches it's OUT fields
                 * without fetching full size file
                 */
                inStream = appContext.contentResolver.openInputStream(imgUri)
                BitmapFactory.decodeStream(inStream, null, this)

                val currentBitmapHeight = outHeight
                val currentBitmapWidth = outWidth

                /*
                 * Sets default mode and the next bitmap  decoding will load the full size file
                 */
                inJustDecodeBounds = false

                var newHeight: Int = currentBitmapHeight
                var newWidth: Int = currentBitmapWidth

                /* Calculates the scale coefficient based on the constant resolutions
                 * for LANDSCAPE and PORTRAIT
                 */

                val scaleDownCoefficient: Float? =
                    //Check if orientations is LANDSCAPE
                    if (currentBitmapWidth > currentBitmapHeight) {
                        //check if it matches to our desired width
                        if (currentBitmapWidth > LANDSCAPE_W) {
                            LANDSCAPE_W.toFloat() / currentBitmapWidth.toFloat()
                        } else {
                            null
                        }
                    }
                    //Orientation is PORTRAIT
                    else {
                        //check if it matches to our desired height
                        if (currentBitmapWidth > PORTRAIT_H) {
                            PORTRAIT_H.toFloat() / currentBitmapHeight.toFloat()
                        } else {
                            null
                        }
                    }

                //Scales old image width and height based on the scale coefficient
                if (scaleDownCoefficient != null) {
                    newWidth = (currentBitmapWidth * scaleDownCoefficient).toInt()
                    newHeight = (currentBitmapHeight * scaleDownCoefficient).toInt()
                }

                invoke(newWidth, newHeight)


            }catch (e: Exception){
                crashlytics.sendNonFatalError(e)
            } finally {
                inStream?.close()
            }

        }
    }

    /**
     * Builds path to the current application cache directory + attach temporary file name
     * and it's extension type.
     *
     * @return path for generation placeholder file for the bitmap compression
     */
    private fun buildTmpFilePath(format: CompressFormat): String =
        "${ContextWrapper(appContext).cacheDir}/${UUID.randomUUID()}${
            getEndpointByCompressionFormat(
                format
            )
        }"

    /**
     * Provides the image endpoint extension based on the [CompressFormat] type.
     *
     * @return the endpoint extension
     */
    private fun getEndpointByCompressionFormat(format: CompressFormat): String {
        return when (format) {
            CompressFormat.PNG -> ".png"
            CompressFormat.JPEG -> ".jpg"
            else -> ".webp"
        }
    }

    /**
     * Copies the image metadata from the old file to the new compressed image file.
     */
    private fun copyExif(placeholderFile: File, originalImgUri: Uri) {
        var oldFile: InputStream? = null
        var newFile: InputStream? = null

        try {
            oldFile = appContext.contentResolver.openInputStream(originalImgUri)
            newFile = appContext.contentResolver.openInputStream(placeholderFile.toUri())

            if (oldFile != null && newFile != null) {
                val originExif = ExifInterface(oldFile)
                val newExif = ExifInterface(newFile)
                for (key in imageAttr) {
                    val originAttributeValue = originExif.getAttribute(key)
                    if (originAttributeValue != null) {
                        newExif.setAttribute(key, originAttributeValue)
                    }
                }
                newExif.saveAttributes()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            oldFile?.close()
            newFile?.close()
        }
    }

}

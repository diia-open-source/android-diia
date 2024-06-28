package ua.gov.diia.core.util.service.exif

import android.content.Context
import androidx.core.net.toUri
import androidx.exifinterface.media.ExifInterface
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.suspendCancellableCoroutine
import ua.gov.diia.core.models.select_location.Coordinate
import java.io.InputStream
import javax.inject.Inject
import kotlin.coroutines.resume

class ExifAttrServiceImpl @Inject constructor(
    @ApplicationContext private val appContext: Context
) : ExifAttrService {

    override suspend fun getAttr(attrKey: String, imgUri: String): String? =
        suspendCancellableCoroutine { cont ->
            var inStream: InputStream? = null

            try {
                inStream = appContext.contentResolver.openInputStream(imgUri.toUri())
                if (inStream != null) {
                    val exif = ExifInterface(inStream)
                    val attr = exif.getAttribute(attrKey)
                    cont.resume(attr)
                } else {
                    cont.resume(null)
                }
            } catch (e: Exception) {
                cont.resume(null)
            } finally {
                inStream?.close()
            }

            cont.invokeOnCancellation {
                inStream?.close()
            }
        }

    override suspend fun getLocationMetadata(imgUri: String): Coordinate? = coroutineScope {
        val latDef = async { getAttr(ExifInterface.TAG_GPS_LATITUDE, imgUri) }
        val lonDef = async { getAttr(ExifInterface.TAG_GPS_LONGITUDE, imgUri) }

        val lat = latDef.await()?.toDoubleOrNull()
        val lon = lonDef.await()?.toDoubleOrNull()

        if (lat != null && lat != 0.0 && lon != null && lon != 0.0) {
            Coordinate(
                latitude = lat,
                longitude = lon
            )
        } else {
            null
        }
    }
}
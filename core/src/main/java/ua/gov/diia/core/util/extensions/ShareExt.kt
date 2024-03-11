package ua.gov.diia.core.util.extensions

import ua.gov.diia.core.models.share.ShareByteArr
import java.io.BufferedInputStream
import java.io.ByteArrayOutputStream
import java.net.URL

fun getByteArrFromUrl(imgUrl: String) = getByteArrFromUrl(getFileNameFromUrl(imgUrl), imgUrl)

fun getByteArrFromUrl(name: String, imgUrl: String): ShareByteArr {
    val url = URL(imgUrl)
    url.openStream().use { inp ->
        BufferedInputStream(inp).use { bis ->
            ByteArrayOutputStream(1024).use { fos ->
                val data = ByteArray(1024)
                var count: Int
                while (bis.read(data, 0, 1024).also { count = it } != -1) {
                    fos.write(data, 0, count)
                }
                return ShareByteArr(name, fos.toByteArray())
            }
        }
    }
}

fun getFileNameFromUrl(imgUrl: String, fileName: String? = null): String {
    return fileName ?: imgUrl.substringAfterLast("/")
}


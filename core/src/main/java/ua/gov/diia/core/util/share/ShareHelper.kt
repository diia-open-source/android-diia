package ua.gov.diia.core.util.share

import androidx.annotation.WorkerThread
import okhttp3.OkHttpClient
import okhttp3.Request
import ua.gov.diia.core.di.data_source.http.UnauthorizedClient
import ua.gov.diia.core.models.share.ShareByteArr
import ua.gov.diia.core.util.delegation.WithCrashlytics
import javax.inject.Inject

class ShareHelper @Inject constructor(
    @UnauthorizedClient private val okHttpClient: OkHttpClient,
    private val crashlytics: WithCrashlytics
) {

    @WorkerThread
    fun getByteArrFromUrl(imgUrl: String) = getByteArrFromUrl(getFileNameFromUrl(imgUrl), imgUrl)

    @WorkerThread
    fun getByteArrFromUrl(name: String, imgUrl: String): ShareByteArr? {
        val request = Request.Builder()
            .url(imgUrl)
            .build()
        return try {
            val response = okHttpClient.newCall(request).execute()
            if (!response.isSuccessful) throw Exception("ShareHelper: Failed to fetch data: ${response.code()}")
            val byteArray = response.body()?.bytes() ?: throw Exception("ShareHelper: Empty response body")
            ShareByteArr(name, byteArray)
        } catch (e: Exception) {
            crashlytics.sendNonFatalError(e)
            null
        }
    }

    fun getFileNameFromUrl(imgUrl: String, fileName: String? = null): String {
        return fileName ?: imgUrl.substringAfterLast("/")
    }
}
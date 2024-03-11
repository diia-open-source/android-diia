package ua.gov.diia.opensource.util.ext

import okhttp3.OkHttpClient
import ua.gov.diia.opensource.data.network.TimeoutConstants
import java.util.concurrent.TimeUnit

fun OkHttpClient.Builder.setTimeout() {
    connectTimeout(TimeoutConstants.CONNECTION_TIMEOUT, TimeUnit.SECONDS)
    writeTimeout(TimeoutConstants.WRITE_TIMEOUT, TimeUnit.SECONDS)
    readTimeout(TimeoutConstants.READ_TIMEOUT, TimeUnit.SECONDS)
}

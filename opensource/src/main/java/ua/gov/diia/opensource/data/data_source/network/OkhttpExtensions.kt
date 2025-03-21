package ua.gov.diia.opensource.data.data_source.network

import okhttp3.OkHttpClient
import ua.gov.diia.core.util.CommonConst.BUILD_TYPE_DEBUG
import ua.gov.diia.core.util.CommonConst.BUILD_TYPE_STAGE
import ua.gov.diia.opensource.BuildConfig
import java.util.concurrent.TimeUnit

fun OkHttpClient.Builder.setTimeout() {
    connectTimeout(TimeoutConstants.CONNECTION_TIMEOUT, TimeUnit.SECONDS)
    writeTimeout(TimeoutConstants.WRITE_TIMEOUT, TimeUnit.SECONDS)
    readTimeout(TimeoutConstants.READ_TIMEOUT, TimeUnit.SECONDS)
}

private fun isInDebugMode(): Boolean {
    return BuildConfig.BUILD_TYPE == BUILD_TYPE_STAGE || BuildConfig.BUILD_TYPE == BUILD_TYPE_DEBUG
}

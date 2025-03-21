package ua.gov.diia.opensource.data.data_source.network.interceptors

import android.os.Build
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import ua.gov.diia.opensource.BuildConfig
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HttpAppInfoHeaderInterceptor @Inject constructor() : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request: Request = chain.request().newBuilder()
            .header(USER_AGENT, "DIIA ${BuildConfig.PLATFORM_TYPE}-${BuildConfig.VERSION_NAME}")
            .header(APP_VERSION, BuildConfig.VERSION_NAME)
            .header(PLATFORM_TYPE, BuildConfig.PLATFORM_TYPE)
            .header(PLATFORM_VERSION, Build.VERSION.RELEASE)
            .build()
        return chain.proceed(request)
    }

    companion object {
        private const val APP_VERSION = "App-Version"
        private const val PLATFORM_TYPE = "Platform-Type"
        private const val PLATFORM_VERSION = "Platform-Version"
        private const val USER_AGENT = "User-Agent"
    }

}
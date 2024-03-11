package ua.gov.diia.opensource.data.network.interceptors

import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import ua.gov.diia.opensource.BuildConfig
import ua.gov.diia.core.util.CommonConst.BUILD_TYPE_DEBUG
import ua.gov.diia.core.util.CommonConst.BUILD_TYPE_STAGE
import ua.gov.diia.opensource.data.network.ApiLogger
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HttpLoggingInterceptor @Inject constructor(
    private val logger: ApiLogger,
) : Interceptor {

    private var httpLoggingInterceptor: HttpLoggingInterceptor? = null

    init {
        if (BuildConfig.BUILD_TYPE == BUILD_TYPE_STAGE || BuildConfig.BUILD_TYPE == BUILD_TYPE_DEBUG) {
            val logging = HttpLoggingInterceptor(logger).apply {
                level = (HttpLoggingInterceptor.Level.BODY)
            }
            httpLoggingInterceptor = logging
        }
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        return httpLoggingInterceptor?.intercept(chain) ?: chain.proceed(chain.request())
    }
}
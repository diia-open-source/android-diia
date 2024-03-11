package ua.gov.diia.opensource.data.network.interceptors

import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import ua.gov.diia.diia_storage.store.repository.authorization.AuthorizationRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HttpMobileUuidInterceptor @Inject constructor(
    private val authorizationRepository: AuthorizationRepository
) : Interceptor {

    private companion object {
        const val MOBILE_UID = "mobile_uid"
    }

    override fun intercept(chain: Interceptor.Chain): Response {

        val request: Request = chain.request()
        return if (request.header(MOBILE_UID) == null) {
            val uid = runBlocking {
                authorizationRepository.getMobileUuid()
            }
            chain.proceed(
                request.newBuilder()
                    .header(MOBILE_UID, uid)
                    .build()
            )
        } else {
            chain.proceed(request)
        }
    }
}
package ua.gov.diia.opensource.data.network.interceptors

import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import ua.gov.diia.diia_storage.store.repository.authorization.AuthorizationRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HttpProlongAuthorizationInterceptor @Inject constructor(
    private val authorizationRepository: AuthorizationRepository
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val currentToken = runBlocking { authorizationRepository.getTokenData() }
        val request = chain.request().newBuilder().addHeaders(currentToken.token).build()
        return chain.proceed(request)
    }

    private fun Request.Builder.addHeaders(token: String) =
        this.apply { header(AUTH, "$AUTH_BEARER $token") }

    private companion object {
        const val AUTH = "Authorization"
        const val AUTH_BEARER = "Bearer"
    }
}
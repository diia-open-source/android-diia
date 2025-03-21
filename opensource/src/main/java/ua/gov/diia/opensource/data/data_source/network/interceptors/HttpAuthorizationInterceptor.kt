package ua.gov.diia.opensource.data.data_source.network.interceptors

import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import retrofit2.HttpException
import ua.gov.diia.core.di.actions.GlobalActionLogout
import ua.gov.diia.core.di.data_source.http.UnauthorizedClient
import ua.gov.diia.core.models.dialogs.TemplateDialogModel
import ua.gov.diia.core.network.apis.ApiAuth
import ua.gov.diia.core.util.delegation.WithBuildConfig
import ua.gov.diia.core.util.event.UiDataEvent
import ua.gov.diia.core.util.event.UiEvent
import ua.gov.diia.diia_storage.store.repository.authorization.AuthorizationRepository
import ua.gov.diia.opensource.di.actions.GlobalActionProlongUser
import ua.gov.diia.opensource.di.util.RefreshLocker
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HttpAuthorizationInterceptor @Inject constructor(
    private val authorizationRepository: AuthorizationRepository,
    @GlobalActionLogout private val actionLogout: MutableLiveData<UiEvent>,
    @GlobalActionProlongUser private val actionUserVerification: MutableLiveData<UiDataEvent<TemplateDialogModel>>,
    @UnauthorizedClient private val apiAuth: ApiAuth,
    private val refreshLocker: RefreshLocker,
    private val withBuildConfig: WithBuildConfig,
) : Interceptor {

    fun getToken() = runBlocking { authorizationRepository.getTokenData() }

    override fun intercept(chain: Interceptor.Chain): Response {
        val currentToken = getToken()
        val originalRequest = chain.request()
        return if (!currentToken.isExpired(withBuildConfig.getTokenLeeway())) {
            chain.proceedAuth(
                originalRequest.newBuilder().addHeaders(currentToken.token).build()
            )
        } else {
            chain.proceed(getRefreshedToken(chain))
        }
    }

    private fun Interceptor.Chain.proceedAuth(request: Request): Response {
        val response = proceed(request)
        if (response.code() == 401) {
            val oldToken = request.header(AUTH)
            if (oldToken != null) {
                proceed(getRefreshedToken(this))
            }
        }
        return response
    }

    @Synchronized
    private fun getRefreshedToken(chain: Interceptor.Chain): Request {
        synchronized(refreshLocker) {
            var request = chain.request()
            val token = getToken()
            if (token.isExpired(withBuildConfig.getTokenLeeway())) {
                runBlocking {
                    try {
                        val newToken = apiAuth.refreshToken("$AUTH_BEARER ${token.token}")
                        when {
                            newToken.token != null -> {
                                authorizationRepository.setToken(newToken.token as String)
                                request =
                                    chain.request().newBuilder().addHeaders(newToken.token as String).build()
                            }
                            newToken.template != null -> {
                                actionUserVerification.postValue(UiDataEvent(newToken.template as TemplateDialogModel))
                            }
                            else -> {
                                throw IllegalStateException("HttpAuthorizationInterceptor: Unable to refresh token. There are no token and template. Received empty body")
                            }
                        }
                    } catch (e: HttpException) {
                        if (e.code() == 401) {
                            logout()
                        }
                    } catch (e: Exception) {
                    }
                }
            } else {
                request = chain.request().newBuilder().addHeaders(token.token).build()
            }
            return request
        }
    }

    private fun logout() {
        actionLogout.postValue(UiEvent())
    }

    private fun Request.Builder.addHeaders(token: String) =
        this.apply { header(AUTH, "$AUTH_BEARER $token") }

    private companion object {
        const val AUTH = "Authorization"
        const val AUTH_BEARER = "Bearer"
    }
}
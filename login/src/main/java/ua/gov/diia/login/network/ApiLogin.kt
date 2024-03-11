package ua.gov.diia.login.network

import retrofit2.http.GET
import retrofit2.http.Query
import ua.gov.diia.core.network.annotation.Analytics
import ua.gov.diia.login.model.LoginToken

interface ApiLogin {

    @Analytics("getAuthenticationToken")
    @GET("api/v3/auth/token")
    suspend fun getAuthenticationToken(
        @Query("processId") processId: String
    ): LoginToken
}
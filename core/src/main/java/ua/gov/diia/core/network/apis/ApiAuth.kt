package ua.gov.diia.core.network.apis

import retrofit2.http.*
import ua.gov.diia.core.models.RefreshToken
import ua.gov.diia.core.models.Token
import ua.gov.diia.core.models.auth.FaceRecoConfig
import ua.gov.diia.core.network.annotation.Analytics

interface ApiAuth {

    @Analytics("facerecoConfig")
    @GET("api/v1/auth/photoid/fld")
    suspend fun getFaceRecoConfig(
        @Query("isLowRamDevice") isLowRamDevice: Boolean,
    ): FaceRecoConfig

    @Analytics("getTestToken")
    @POST("api/v1/auth/test/{requestId}/token")
    suspend fun getTestToken(
        @Path("requestId") requestID: String?,
        @QueryMap userInfo: Map<String, String>
    ): Token

    @Analytics("refreshToken")
    @POST("api/v2/auth/token/refresh")
    suspend fun refreshToken(@Header("Authorization") token: String?): RefreshToken


    @Analytics("logout")
    @POST("api/v2/auth/token/logout")
    suspend fun logout(
        @Header("Authorization") token: String,
        @Header("mobile_uid") mobileUid: String
    )

    @Analytics("logoutServiceUser")
    @POST("api/v1/auth/acquirer/branch/offer/token/logout")
    suspend fun logoutServiceUser(
        @Header("Authorization") token: String,
        @Header("mobile_uid") mobileUid: String
    )

    @Analytics("tempToken")
    @GET("api/v1/auth/acquirer/branch/offer/{uuid}/token")
    suspend fun getServiceAccountToken(
        @Path("uuid") uuid: String,
        @Header("mobile_uid") mobileUid: String,
    ): Token

}

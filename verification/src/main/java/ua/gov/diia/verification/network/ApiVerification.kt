package ua.gov.diia.verification.network

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import ua.gov.diia.core.models.dialogs.TemplateDialogModelWithProcessCode
import ua.gov.diia.core.network.annotation.Analytics
import ua.gov.diia.verification.model.VerificationMethodsData
import ua.gov.diia.verification.model.VerificationUrl

interface ApiVerification {

    @Analytics("getVerificationMethods")
    @GET("api/v3/auth/{authSchema}/methods")
    suspend fun getVerificationMethods(
        @Path("authSchema") schema: String,
        @Query("processId") processId: String?
    ): VerificationMethodsData

    @Analytics("getAuthUrl")
    @GET("api/v3/auth/{method}/auth-url")
    suspend fun getAuthUrl(
        @Path("method") verificationMethodCode: String,
        @Query("processId") processId: String,
        @Query("bankId") bankCode: String?
    ): VerificationUrl

    @Analytics("completeVerificationStep")
    @GET("api/v1/auth/{method}/{requestId}/verify")
    suspend fun completeVerificationStep(
        @Path("method") verificationMethodCode: String,
        @Path("requestId") requestId: String,
        @Query("processId") processId: String,
        @Query("bankId") bankCode: String?
    ): TemplateDialogModelWithProcessCode

}
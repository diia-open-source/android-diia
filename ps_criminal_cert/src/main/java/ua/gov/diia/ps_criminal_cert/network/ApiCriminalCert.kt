package ua.gov.diia.ps_criminal_cert.network

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import ua.gov.diia.ps_criminal_cert.models.request.CriminalCertConfirmationRequest
import ua.gov.diia.ps_criminal_cert.models.response.CriminalCertBirthPlace
import ua.gov.diia.ps_criminal_cert.models.response.CriminalCertConfirmation
import ua.gov.diia.ps_criminal_cert.models.response.CriminalCertConfirmed
import ua.gov.diia.ps_criminal_cert.models.response.CriminalCertContacts
import ua.gov.diia.ps_criminal_cert.models.response.CriminalCertDetails
import ua.gov.diia.ps_criminal_cert.models.response.CriminalCertFileData
import ua.gov.diia.ps_criminal_cert.models.response.CriminalCertInfo
import ua.gov.diia.ps_criminal_cert.models.response.CriminalCertListData
import ua.gov.diia.ps_criminal_cert.models.response.CriminalCertNationalities
import ua.gov.diia.ps_criminal_cert.models.response.CriminalCertReasons
import ua.gov.diia.ps_criminal_cert.models.response.CriminalCertRequester
import ua.gov.diia.ps_criminal_cert.models.response.CriminalCertTypes
import ua.gov.diia.core.network.annotation.Analytics

interface ApiCriminalCert {

    @Analytics("getCriminalCertList")
    @GET("api/v1/public-service/criminal-cert/applications/{applicationStatus}")
    suspend fun getCriminalCertList(
        @Path("applicationStatus") certStatus: String,
        @Query("skip") skip: Int,
        @Query("limit") limit: Int
    ): CriminalCertListData

    @Analytics("getCriminalCertsDetails")
    @GET("api/v1/public-service/criminal-cert/{certId}")
    suspend fun getCriminalCertsDetails(
        @Path("certId") id: String
    ): CriminalCertDetails

    @Analytics("getCriminalCertPdf")
    @GET("api/v1/public-service/criminal-cert/{certId}/pdf")
    suspend fun getCriminalCertPdf(
        @Path("certId") certId: String
    ): CriminalCertFileData

    @Analytics("getCriminalCertZip")
    @GET("api/v1/public-service/criminal-cert/{certId}/download")
    suspend fun getCriminalCertZip(
        @Path("certId") certId: String
    ): CriminalCertFileData

    @Analytics("getCriminalCertInfo")
    @GET("api/v1/public-service/criminal-cert/application/info")
    suspend fun getCriminalCertInfo(
        @Query("publicService") publicService: String?
    ): CriminalCertInfo

    @Analytics("getCriminalCertReasons")
    @GET("api/v1/public-service/criminal-cert/reasons")
    suspend fun getCriminalCertReasons(): CriminalCertReasons

    @Analytics("getCriminalCertTypes")
    @GET("api/v1/public-service/criminal-cert/types")
    suspend fun getCriminalCertTypes(): CriminalCertTypes

    @Analytics("getCriminalCertRequester")
    @GET("api/v1/public-service/criminal-cert/requester")
    suspend fun getCriminalCertRequester(): CriminalCertRequester

    @Analytics("getCriminalCertBirthPlace")
    @GET("api/v1/public-service/criminal-cert/birth-place")
    suspend fun getCriminalCertBirthPlace(): CriminalCertBirthPlace

    @Analytics("getCriminalCertNationalities")
    @GET("api/v1/public-service/criminal-cert/nationalities")
    suspend fun getCriminalCertNationalities(): CriminalCertNationalities

    @Analytics("getCriminalCertContacts")
    @GET("api/v1/public-service/criminal-cert/contacts")
    suspend fun getCriminalCertContacts(): CriminalCertContacts

    @Analytics("getCriminalCertConfirmationData")
    @POST("api/v1/public-service/criminal-cert/confirmation")
    suspend fun getCriminalCertConfirmationData(
        @Body body: CriminalCertConfirmationRequest
    ): CriminalCertConfirmation

    @Analytics("confirmCriminalCert")
    @POST("api/v1/public-service/criminal-cert/application")
    suspend fun orderCriminalCert(
        @Body body: CriminalCertConfirmationRequest
    ): CriminalCertConfirmed
}
package ua.gov.diia.opensource.data.data_source.network.api

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap
import ua.gov.diia.core.models.ITN
import ua.gov.diia.core.models.common.DetailsPDF
import ua.gov.diia.core.network.annotation.Analytics
import ua.gov.diia.documents.models.DocumentsOrder
import ua.gov.diia.core.models.document.ManualDocs
import ua.gov.diia.documents.models.QRUrl
import ua.gov.diia.documents.models.TypeDefinedDocumentsOrder
import ua.gov.diia.documents.models.VerificationCodesOrgResponse
import ua.gov.diia.opensource.model.documents.Docs

interface ApiDocs {

    @Analytics("getDocs")
    @GET("api/v6/documents")
    suspend fun getDocs(@QueryMap filter: Map<String, String> = emptyMap()): Docs

    @Analytics("getDocsManual")
    @GET("api/v1/documents/manual")
    suspend fun getDocsManual(): ManualDocs

    @Analytics("setDocumentsOrder")
    @POST("api/v2/user/settings/documents/order")
    suspend fun setDocumentsOrder(
        @Body docOrder: DocumentsOrder
    )

    @Analytics("setTypedDocumentsOrder")
    @POST("api/v2/user/settings/documents/{documentType}/order")
    suspend fun setTypedDocumentsOrder(
        @Path("documentType") documentType: String,
        @Body docOrder: TypeDefinedDocumentsOrder
    )

    @Analytics("getItn")
    @GET("api/v1/documents/itn")
    suspend fun getItn(): ITN

    @Analytics("getShareUrl")
    @GET("api/v1/documents/{docName}/{documentId}/share")
    suspend fun getShareUrl(
        @Path("docName") docName: String?,
        @Path("documentId") documentId: String?
    ): QRUrl

    @Analytics("getDocumentPdf")
    @GET("api/v1/documents/{documentType}/{docId}/download")
    suspend fun getDocumentPdf(
        @Path("documentType") documentType: String,
        @Path("docId") docId: String,
    ): DetailsPDF

    @Analytics("getShareUrlWithLocalization")
    @GET("api/v1/documents/{docName}/{documentId}/share")
    suspend fun getShareUrlWithLocalization(
        @Path("docName") docName: String?,
        @Path("documentId") documentId: String?,
        @Query("localization") localization: String,
    ): QRUrl

    @Analytics("getVerificationCodesOrg")
    @GET("api/v2/documents/{documentType}/{documentId}/share")
    suspend fun getVerificationCodesOrg(
        @Path("documentType") documentType: String?,
        @Path("documentId") documentId: String?,
        @Query("localization") localization: String,
    ): VerificationCodesOrgResponse

}
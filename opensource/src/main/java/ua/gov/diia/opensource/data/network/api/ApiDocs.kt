package ua.gov.diia.opensource.data.network.api

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap
import ua.gov.diia.core.network.annotation.Analytics
import ua.gov.diia.doc_driver_license.DriverLicenseV2
import ua.gov.diia.documents.models.DocumentsOrder
import ua.gov.diia.documents.models.ManualDocs
import ua.gov.diia.documents.models.QRUrl
import ua.gov.diia.documents.models.TypeDefinedDocumentsOrder
import ua.gov.diia.documents.models.UpdatedDoc
import ua.gov.diia.opensource.helper.documents.DocName
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

    @Analytics("getShareUrl")
    @GET("api/v1/documents/{docName}/{documentId}/share")
    suspend fun getShareUrl(
        @Path("docName") docName: String?,
        @Path("documentId") documentId: String?
    ): QRUrl

    @Analytics("getShareUrlWithLocalization")
    @GET("api/v1/documents/{docName}/{documentId}/share")
    suspend fun getShareUrlWithLocalization(
        @Path("docName") docName: String?,
        @Path("documentId") documentId: String?,
        @Query("localization") localization: String,
    ): QRUrl

    @Analytics("getDocumentById")
    @GET("api/v2/documents/{docType}/{documentId}")
    suspend fun getDocumentById(
        @Path("docType") docType: String?,
        @Path("documentId") documentId: String?
    ): UpdatedDoc

    @Analytics("checkDriverLicense")
    @GET("api/v2/documents/${DocName.DRIVER_LICENSE}/verify")
    suspend fun checkDriverLicense(@Query("otp") otp: String): DriverLicenseV2.Data
}
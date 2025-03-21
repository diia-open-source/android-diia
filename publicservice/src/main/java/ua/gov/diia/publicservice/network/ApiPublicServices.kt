package ua.gov.diia.publicservice.network

import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import ua.gov.diia.core.models.SuccessResponse
import ua.gov.diia.core.models.dialogs.TemplateDialogModelWithProcessCode
import ua.gov.diia.core.models.share.ShareDataResponse
import ua.gov.diia.core.network.annotation.Analytics
import ua.gov.diia.publicservice.models.PublicServicesCategories

interface ApiPublicServices {

    @Analytics("getPublicServices")
    @GET("api/v3/public-service/catalog")
    suspend fun getPublicServices(): PublicServicesCategories

    @Analytics("checkPromo")
    @GET("api/v1/public-service/promo")
    suspend fun checkPromo(): TemplateDialogModelWithProcessCode

    @Analytics("subscribeToBeta")
    @POST("api/v1/user/subscription/public-service")
    suspend fun subscribeToBeta(
        @Query("segmentId") segmentId: Int?
    ): SuccessResponse

    @Analytics("getFindEnemyShareLink")
    @GET("api/v1/public-service/enemy-track/link")
    suspend fun getFindEnemyShareLink(): ShareDataResponse
}

package ua.gov.diia.feed.network

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import ua.gov.diia.core.models.common_compose.general.DiiaResponse
import ua.gov.diia.core.models.share.ShareDataResponse
import ua.gov.diia.core.network.annotation.Analytics
import ua.gov.diia.feed.models.News

interface ApiFeed {
    @Analytics("getFeed")
    @GET("api/v1/feed")
    suspend fun getFeed(): DiiaResponse

    @Analytics("getFinEnemyShareLink")
    @GET("api/v1/public-service/enemy-track/link")
    suspend fun getRedirectLink(): ShareDataResponse

    @Analytics("getNewsScreen")
    @GET("api/v1/feed/news/screen")
    suspend fun getAllNewsScreen(): DiiaResponse

    @Analytics("getNews")
    @GET("api/v1/feed/news")
    suspend fun getNews(
        @Query("skip") offset: Int,
        @Query("limit") size: Int
    ): News

    @Analytics("getNewsById")
    @GET("api/v1/feed/news/{id}")
    suspend fun getNewsById(
        @Path("id") id: String
    ): DiiaResponse

}
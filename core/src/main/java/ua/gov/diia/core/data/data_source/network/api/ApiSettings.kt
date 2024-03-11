package ua.gov.diia.core.data.data_source.network.api

import retrofit2.http.GET
import ua.gov.diia.core.models.appversion.AppSettingsInfo
import ua.gov.diia.core.network.annotation.Analytics

interface ApiSettings {

    @Analytics("getAppSettingsInfo")
    @GET("api/v1/settings")
    suspend fun appSettingsInfo(): AppSettingsInfo

}

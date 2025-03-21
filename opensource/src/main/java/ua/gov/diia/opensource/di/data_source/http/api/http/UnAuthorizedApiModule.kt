package ua.gov.diia.opensource.di.data_source.http.api.http

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import ua.gov.diia.core.data.data_source.network.api.ApiSettings
import ua.gov.diia.core.data.data_source.network.api.notification.ApiNotificationsPublic
import ua.gov.diia.core.di.data_source.http.UnauthorizedClient
import ua.gov.diia.core.network.apis.ApiAuth

@Module
@InstallIn(SingletonComponent::class)
object UnAuthorizedApiModule {

    @Provides
    @UnauthorizedClient
    fun provideApiAuth(
        @UnauthorizedClient retrofit: Retrofit
    ): ApiAuth = retrofit.create(ApiAuth::class.java)

    @Provides
    @UnauthorizedClient
    fun provideApiNotificationPublic(
        @UnauthorizedClient retrofit: Retrofit
    ): ApiNotificationsPublic = retrofit.create(ApiNotificationsPublic::class.java)

    @Provides
    @UnauthorizedClient
    fun provideApiSettings(
        @UnauthorizedClient retrofit: Retrofit
    ): ApiSettings = retrofit.create(ApiSettings::class.java)

}
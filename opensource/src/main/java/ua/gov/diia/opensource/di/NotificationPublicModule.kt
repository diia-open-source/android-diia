package ua.gov.diia.opensource.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import ua.gov.diia.core.data.data_source.network.api.notification.ApiNotificationsPublic
import ua.gov.diia.core.di.data_source.http.AuthorizedClient
import ua.gov.diia.core.di.data_source.http.UnauthorizedClient

@Module
@InstallIn(SingletonComponent::class)
object NotificationPublicModule {

    @Provides
    @AuthorizedClient
    fun provideApiNotificationPublicAuthorized(
        @AuthorizedClient retrofit: Retrofit
    ): ApiNotificationsPublic = retrofit.create(ApiNotificationsPublic::class.java)

    @Provides
    @UnauthorizedClient
    fun provideApiNotificationPublicUnauthorized(
        @UnauthorizedClient retrofit: Retrofit
    ): ApiNotificationsPublic = retrofit.create(ApiNotificationsPublic::class.java)
}
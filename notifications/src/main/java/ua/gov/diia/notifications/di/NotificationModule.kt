package ua.gov.diia.notifications.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.create
import ua.gov.diia.core.di.data_source.http.AuthorizedClient
import ua.gov.diia.core.di.data_source.http.UnauthorizedClient
import ua.gov.diia.notifications.data.data_source.network.api.notification.ApiNotifications

@Module
@InstallIn(SingletonComponent::class)
object NotificationModule {

    @Provides
    @UnauthorizedClient
    fun provideNotificationApi(
        @UnauthorizedClient retrofit: Retrofit
    ): ApiNotifications = retrofit.create()

    @Provides
    @AuthorizedClient
    fun provideApiNotifications(
        @AuthorizedClient retrofit: Retrofit
    ): ApiNotifications = retrofit.create()
}
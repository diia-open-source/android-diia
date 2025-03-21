package ua.gov.diia.opensource.di.data_source.http.api.http

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import ua.gov.diia.core.data.data_source.network.api.notification.ApiNotificationsPublic
import ua.gov.diia.core.di.data_source.http.AuthorizedClient
import ua.gov.diia.opensource.data.data_source.network.api.ApiDocs

@Module
@InstallIn(SingletonComponent::class)
object AuthorizedApiModule {

    @Provides
    @AuthorizedClient
    fun provideApiPrimaryUserDoc(
        @AuthorizedClient retrofit: Retrofit
    ): ApiDocs = retrofit.create(ApiDocs::class.java)

    @Provides
    @AuthorizedClient
    fun provideApiNotificationPublic(
        @AuthorizedClient retrofit: Retrofit
    ): ApiNotificationsPublic = retrofit.create(ApiNotificationsPublic::class.java)

}
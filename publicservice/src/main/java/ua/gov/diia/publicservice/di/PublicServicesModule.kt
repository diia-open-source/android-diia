package ua.gov.diia.publicservice.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import ua.gov.diia.core.di.data_source.http.AuthorizedClient
import ua.gov.diia.publicservice.network.ApiPublicServices

@Module
@InstallIn(SingletonComponent::class)
object PublicServicesModule {

    @Provides
    @AuthorizedClient
    fun provideApiPublicServices(
        @AuthorizedClient retrofit: Retrofit
    ): ApiPublicServices = retrofit.create(ApiPublicServices::class.java)
}
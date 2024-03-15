package ua.gov.diia.opensource.di.network

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.create
import ua.gov.diia.core.data.data_source.network.api.ApiSettings
import ua.gov.diia.core.di.data_source.http.UnauthorizedClient

@Module
@InstallIn(SingletonComponent::class)
object UnAuthorizedApiModule {

    @Provides
    @UnauthorizedClient
    fun provideApiSettings(
        @UnauthorizedClient retrofit: Retrofit
    ): ApiSettings = retrofit.create()
}
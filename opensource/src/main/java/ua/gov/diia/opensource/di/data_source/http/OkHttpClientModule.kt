package ua.gov.diia.opensource.di.data_source.http

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import ua.gov.diia.core.di.data_source.http.AuthorizedClient
import ua.gov.diia.core.di.data_source.http.ProlongClient
import ua.gov.diia.core.di.data_source.http.UnauthorizedClient
import ua.gov.diia.opensource.data.data_source.network.interceptors.HttpAppInfoHeaderInterceptor
import ua.gov.diia.opensource.data.data_source.network.interceptors.HttpAuthorizationInterceptor
import ua.gov.diia.opensource.data.data_source.network.interceptors.HttpLoggingInterceptor
import ua.gov.diia.opensource.data.data_source.network.interceptors.HttpMobileUuidInterceptor
import ua.gov.diia.opensource.data.data_source.network.interceptors.HttpProlongAuthorizationInterceptor
import ua.gov.diia.opensource.data.data_source.network.setTimeout
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object OkHttpClientModule {

    @Provides
    @UnauthorizedClient
    @Singleton
    fun provideUnauthorizedOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
        appInfoHeaderInterceptor: HttpAppInfoHeaderInterceptor,
        uuidInterceptor: HttpMobileUuidInterceptor
    ): OkHttpClient = OkHttpClient.Builder()
        .apply {
            addInterceptor(appInfoHeaderInterceptor)
            addInterceptor(uuidInterceptor)
            addInterceptor(loggingInterceptor)
            setTimeout()
        }.build()

    @Provides
    @AuthorizedClient
    @Singleton
    fun provideAuthorizedOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
        appInfoHeaderInterceptor: HttpAppInfoHeaderInterceptor,
        uuidInterceptor: HttpMobileUuidInterceptor,
        authorizationInterceptor: HttpAuthorizationInterceptor
    ): OkHttpClient = OkHttpClient.Builder()
        .apply {
            addInterceptor(appInfoHeaderInterceptor)
            addInterceptor(uuidInterceptor)
            addInterceptor(authorizationInterceptor)
            addInterceptor(loggingInterceptor)
            setTimeout()
        }.build()

    @Provides
    @ProlongClient
    @Singleton
    fun provideProlongOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
        appInfoHeaderInterceptor: HttpAppInfoHeaderInterceptor,
        uuidInterceptor: HttpMobileUuidInterceptor,
        prolongAuthorizationInterceptor: HttpProlongAuthorizationInterceptor
    ): OkHttpClient = OkHttpClient.Builder()
        .apply {
            addInterceptor(appInfoHeaderInterceptor)
            addInterceptor(uuidInterceptor)
            addInterceptor(prolongAuthorizationInterceptor)
            addInterceptor(loggingInterceptor)
            setTimeout()
        }.build()
}
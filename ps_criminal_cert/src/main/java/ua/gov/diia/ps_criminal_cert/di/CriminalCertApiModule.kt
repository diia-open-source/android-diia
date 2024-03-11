package ua.gov.diia.ps_criminal_cert.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import ua.gov.diia.core.di.data_source.http.AuthorizedClient
import ua.gov.diia.ps_criminal_cert.network.ApiCriminalCert

@Module
@InstallIn(SingletonComponent::class)
object CriminalCertApiModule {

    @Provides
    @AuthorizedClient
    fun provideApiCriminalCert(
        @AuthorizedClient retrofit: Retrofit
    ): ApiCriminalCert = retrofit.create(ApiCriminalCert::class.java)
}
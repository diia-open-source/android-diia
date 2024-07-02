package ua.gov.diia.address_search.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.create
import ua.gov.diia.address_search.network.ApiAddressSearch
import ua.gov.diia.core.di.data_source.http.AuthorizedClient

@Module
@InstallIn(SingletonComponent::class)
object AddressSearchApiModule {

    @Provides
    @AuthorizedClient
    fun provideApiAddressSearch(
        @AuthorizedClient retrofit: Retrofit
    ): ApiAddressSearch = retrofit.create()
}
package ua.gov.diia.feed.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import ua.gov.diia.core.di.data_source.http.AuthorizedClient
import ua.gov.diia.feed.network.ApiFeed

@Module
@InstallIn(SingletonComponent::class)
object FeedApiModule {

    @Provides
    @AuthorizedClient
    fun provideApiFeed(
        @AuthorizedClient retrofit: Retrofit
    ): ApiFeed = retrofit.create(ApiFeed::class.java)
}
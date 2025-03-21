package ua.gov.diia.opensource.di.feature

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ua.gov.diia.feed.helper.FeedHelper
import ua.gov.diia.feed.helper.FeedOfflineScreenContentProvider
import ua.gov.diia.opensource.helper.FeedHelperImpl

@Module
@InstallIn(SingletonComponent::class)
object FeedModule {

    @Provides
    fun provideFeedHelper(): FeedHelper {
        return FeedHelperImpl()
    }

    @Provides
    fun provideFeedOfflineScreenContentProvider(): FeedOfflineScreenContentProvider {
        return FeedOfflineScreenContentProviderImpl()
    }
}
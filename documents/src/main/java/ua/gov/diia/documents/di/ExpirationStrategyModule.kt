package ua.gov.diia.documents.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ua.gov.diia.core.util.date.CurrentDateProvider
import ua.gov.diia.documents.util.datasource.*
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ExpirationStrategyModule {

    @Provides
    @Singleton
    fun provideExpirationStrategy(
        currentDateProvider: CurrentDateProvider,
    ): ExpirationStrategy = DateCompareExpirationStrategy(currentDateProvider)
}
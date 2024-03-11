package ua.gov.diia.core.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ua.gov.diia.core.util.date.CurrentDateProvider
import ua.gov.diia.core.util.date.CurrentDateProviderImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DateProviderModule {

    @Provides
    @Singleton
    fun provideDateProvider(): CurrentDateProvider = CurrentDateProviderImpl()
}
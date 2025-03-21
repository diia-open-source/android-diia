package ua.gov.diia.opensource.di.util

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RefreshLockerModule {

    @Provides
    @Singleton
    fun provideRefreshLocker(
    ): RefreshLocker {
        return RefreshLocker()
    }
}

class RefreshLocker()
package ua.gov.diia.diia_storage.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ua.gov.diia.diia_storage.store.datasource.preferences.KotlinStoreImpl
import ua.gov.diia.diia_storage.store.datasource.preferences.PreferenceDataSource
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PreferenceStorage {

    @Provides
    @Singleton
    fun providePreferenceStorage(
        @ApplicationContext appContext: Context
    ): PreferenceDataSource = KotlinStoreImpl(appContext)
}
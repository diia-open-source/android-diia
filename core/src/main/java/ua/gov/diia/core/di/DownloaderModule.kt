package ua.gov.diia.core.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ua.gov.diia.core.util.file_download.Downloader
import ua.gov.diia.core.util.file_download.DownloaderImpl


@Module
@InstallIn(SingletonComponent::class)
object DownloaderModule {

    @Provides
    fun provideDownloader(@ApplicationContext appContext: Context): Downloader {
        return DownloaderImpl(appContext)
    }
}
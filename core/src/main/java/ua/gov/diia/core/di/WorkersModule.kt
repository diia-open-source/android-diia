package ua.gov.diia.core.di

import android.content.Context
import androidx.work.WorkManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.ElementsIntoSet
import ua.gov.diia.core.util.work.CheckAppVersionUpdatedWork
import ua.gov.diia.core.util.work.DoApplicationSettingsProvisionWork
import ua.gov.diia.core.util.work.WorkScheduler

@Module
@InstallIn(SingletonComponent::class)
object WorkersModule {

    @Provides
    fun provideWorkManager(
        @ApplicationContext appContext: Context
    ): WorkManager = WorkManager.getInstance(appContext)

    @Provides
    @ElementsIntoSet
    fun provideWorkSchedulers(): Set<@JvmSuppressWildcards WorkScheduler> = setOf(
        DoApplicationSettingsProvisionWork,
        CheckAppVersionUpdatedWork,
    )
}
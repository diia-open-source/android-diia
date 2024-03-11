package ua.gov.diia.analytics.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ua.gov.diia.analytics.DiiaAnalytics
import ua.gov.diia.analytics.DiiaAnalyticsImpl
import ua.gov.diia.analytics.crashlytics.WithCrashlyticsImpl
import ua.gov.diia.core.util.delegation.WithCrashlytics
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AnalyticsModule {

    @Provides
    @Singleton
    fun provideDiiaAnalytics(
        @ApplicationContext context: Context
    ): DiiaAnalytics = DiiaAnalyticsImpl(context)

    @Provides
    @Singleton
    fun provideCrashlytics(): WithCrashlytics = WithCrashlyticsImpl()
}

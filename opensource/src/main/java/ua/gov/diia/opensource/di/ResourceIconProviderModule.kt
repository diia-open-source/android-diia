package ua.gov.diia.opensource.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ua.gov.diia.opensource.ui.compose.DiiaResourceIconProviderImpl
import ua.gov.diia.ui_base.components.DiiaResourceIconProvider
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ResourceIconProviderModule {

    @Provides
    @Singleton
    fun provideDiiaResourceIconProvider(): DiiaResourceIconProvider {
        return DiiaResourceIconProviderImpl()
    }
}
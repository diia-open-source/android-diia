package ua.gov.diia.opensource.di.feature

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ua.gov.diia.opensource.helper.SplashHelperImpl
import ua.gov.diia.splash.helper.SplashHelper

@Module
@InstallIn(SingletonComponent::class)
interface SplashModule {

    @Binds
    fun bindSplashHelper(impl: SplashHelperImpl): SplashHelper
}
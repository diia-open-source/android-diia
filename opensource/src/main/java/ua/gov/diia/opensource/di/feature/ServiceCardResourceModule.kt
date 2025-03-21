package ua.gov.diia.opensource.di.feature

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ua.gov.diia.opensource.helper.ServiceCardResourceHelperImpl
import ua.gov.diia.ui_base.helper.ServiceCardResourceHelper

@Module
@InstallIn(SingletonComponent::class)
object ServiceCardResourceModule {

    @Provides
    fun provideServiceCardResourceHelper(): ServiceCardResourceHelper {
        return ServiceCardResourceHelperImpl()
    }
}
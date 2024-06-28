package ua.gov.diia.ui_base.di

import dagger.Module
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ua.gov.diia.ui_base.helper.ServiceCardResourceHelper

@Module
@InstallIn(SingletonComponent::class)
object ServiceCardResourceEntryPointModule {

    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface ServiceCardResourceEntryPoint {
        val serviceCardResourceHelper: ServiceCardResourceHelper
    }

}
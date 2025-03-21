package ua.gov.diia.opensource.di.feature

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ua.gov.diia.opensource.helper.PinHelperImpl
import ua.gov.diia.pin.helper.PinHelper

@Module
@InstallIn(SingletonComponent::class)
interface PinModule {

    @Binds
    fun bindPinHelper(impl: PinHelperImpl): PinHelper
}
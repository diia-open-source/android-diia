package ua.gov.diia.opensource.di.feature

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import ua.gov.diia.opensource.helper.PSNavigationHelperImpl
import ua.gov.diia.publicservice.helper.PSNavigationHelper

@Module
@InstallIn(ViewModelComponent::class)
class PSNavigationModule {

    @Provides
    fun providePSNavigationHelper(): PSNavigationHelper {
        return PSNavigationHelperImpl()
    }
}
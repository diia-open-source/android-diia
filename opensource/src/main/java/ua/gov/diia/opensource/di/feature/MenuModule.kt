package ua.gov.diia.opensource.di.feature

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ua.gov.diia.menu.helper.MenuHelper
import ua.gov.diia.opensource.helper.MenuHelperImpl

@Module
@InstallIn(SingletonComponent::class)
object MenuModule {

    @Provides
    fun provideMenuHelper(): MenuHelper {
        return MenuHelperImpl()
    }
}
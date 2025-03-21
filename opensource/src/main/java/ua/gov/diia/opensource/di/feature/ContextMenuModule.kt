package ua.gov.diia.opensource.di.feature

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ua.gov.diia.context_menu.helper.ContextMenuNavigationHelper
import ua.gov.diia.opensource.helper.ContextMenuNavigationHelperImpl

@Module
@InstallIn(SingletonComponent::class)
object ContextMenuModule {

    @Provides
    fun provideContextMenuNavigationHelper(): ContextMenuNavigationHelper {
        return ContextMenuNavigationHelperImpl()
    }

}
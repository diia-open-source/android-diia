package ua.gov.diia.home.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import ua.gov.diia.home.ui.HomeScreenComposeMapper
import ua.gov.diia.home.ui.HomeScreenComposeMapperImpl

@Module
@InstallIn(ViewModelComponent::class)
interface HomeScreenTabsMappersModule {

    @Binds
    fun bindHomeScreenComposeMapper(
        impl: HomeScreenComposeMapperImpl
    ): HomeScreenComposeMapper

}
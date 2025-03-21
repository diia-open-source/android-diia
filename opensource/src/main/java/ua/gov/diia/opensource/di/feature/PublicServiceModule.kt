package ua.gov.diia.opensource.di.feature

import dagger.Provides
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import ua.gov.diia.opensource.helper.PublicServiceHelperImpl
import ua.gov.diia.opensource.helper.PublicServicesCategoriesTabMapperImpl
import ua.gov.diia.publicservice.helper.PublicServiceHelper
import ua.gov.diia.publicservice.ui.categories.compose.PublicServicesCategoriesTabMapper
import ua.gov.diia.publicservice.ui.compose.PublicServiceCategoryDetailsComposeMapper
import ua.gov.diia.publicservice.ui.compose.PublicServiceCategoryDetailsComposeMapperImpl
import ua.gov.diia.publicservice.ui.compose.PublicServicesSearchComposeMapper
import ua.gov.diia.publicservice.ui.compose.PublicServicesSearchComposeMapperImpl

@Module
@InstallIn(ViewModelComponent::class)
class PublicServiceModule {

    @Provides
    fun providePublicServiceHelper(): PublicServiceHelper {
        return PublicServiceHelperImpl()
    }

    @Provides
    fun providePublicServicesTabMapper(): PublicServicesCategoriesTabMapper {
        return PublicServicesCategoriesTabMapperImpl()
    }

    @Provides
    fun providePublicServiceCategoryDetailsMapper(): PublicServiceCategoryDetailsComposeMapper {
        return PublicServiceCategoryDetailsComposeMapperImpl()
    }

    @Provides
    fun providePublicServicesSearchComposeMapper(): PublicServicesSearchComposeMapper {
        return PublicServicesSearchComposeMapperImpl()
    }
}
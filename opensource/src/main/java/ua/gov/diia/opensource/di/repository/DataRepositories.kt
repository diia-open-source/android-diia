package ua.gov.diia.opensource.di.repository

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet
import ua.gov.diia.core.data.repository.DataRepository
import ua.gov.diia.login.ui.PostLoginAction
import ua.gov.diia.opensource.data.repository.data.PublicServiceDataRepository
import ua.gov.diia.publicservice.di.DataRepositoryPublicServiceCategories
import ua.gov.diia.publicservice.models.PublicServicesCategories
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DataRepositories {

    @Binds
    @Singleton
    @DataRepositoryPublicServiceCategories
    fun bindPublicServiceCategoriesRepository(
        impl: PublicServiceDataRepository
    ): DataRepository<@JvmSuppressWildcards PublicServicesCategories?>

    @Binds
    @IntoSet
    fun bindPublicServiceCategoriesRepositoryPostLogin(
        impl: PublicServiceDataRepository
    ): PostLoginAction
}
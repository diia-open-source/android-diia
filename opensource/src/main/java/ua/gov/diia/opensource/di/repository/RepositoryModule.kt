package ua.gov.diia.opensource.di.repository

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ua.gov.diia.core.data.repository.SystemRepository
import ua.gov.diia.diia_storage.store.repository.authorization.AuthorizationRepository
import ua.gov.diia.diia_storage.store.repository.authorization.AuthorizationRepositoryImpl
import ua.gov.diia.diia_storage.store.repository.system.SystemRepositoryImpl
import ua.gov.diia.opensource.data.repository.settings.AppSettingsRepository
import ua.gov.diia.opensource.data.repository.settings.AppSettingsRepositoryImpl

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @Binds
    fun bindSystemRepository(impl: SystemRepositoryImpl): SystemRepository

    @Binds
    fun bindSettingsRepository(impl: AuthorizationRepositoryImpl): AuthorizationRepository

    @Binds
    fun bindAppSettingsRepository(impl: AppSettingsRepositoryImpl): AppSettingsRepository
}
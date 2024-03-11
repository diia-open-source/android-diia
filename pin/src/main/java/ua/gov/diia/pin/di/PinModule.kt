package ua.gov.diia.pin.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ua.gov.diia.pin.repository.LoginPinRepository
import ua.gov.diia.pin.repository.LoginPinRepositoryImpl

@Module
@InstallIn(SingletonComponent::class)
interface PinModule {

    @Binds
    fun bindLoginPinRepository(impl: LoginPinRepositoryImpl): LoginPinRepository
}
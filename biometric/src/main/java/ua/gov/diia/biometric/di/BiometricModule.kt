package ua.gov.diia.biometric.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ua.gov.diia.biometric.AndroidBiometric
import ua.gov.diia.biometric.Biometric
import ua.gov.diia.biometric.store.BiometricRepository
import ua.gov.diia.biometric.store.BiometricRepositoryImpl

@Module
@InstallIn(SingletonComponent::class)
interface BiometricModule {

    @Binds
    fun bindsBiometricRepository(
        impl: BiometricRepositoryImpl,
    ): BiometricRepository

    @Binds
    fun bindsBiometric(
        impl: AndroidBiometric,
    ): Biometric
}
package ua.gov.diia.opensource.di.delegate

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ua.gov.diia.core.util.delegation.WithAppConfig
import ua.gov.diia.core.util.delegation.WithBuildConfig
import ua.gov.diia.opensource.util.delegation.WithAppConfigImpl
import ua.gov.diia.opensource.util.delegation.WithBuildConfigImpl

@Module
@InstallIn(SingletonComponent::class)
interface AppDelegationModule {

    @Binds
    fun bindWithBuildConfig(
        impl: WithBuildConfigImpl
    ): WithBuildConfig

    @Binds
    fun bindWithAppConfig(
        impl: WithAppConfigImpl
    ): WithAppConfig
}
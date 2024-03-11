package ua.gov.diia.core.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import ua.gov.diia.core.util.system.application.ApplicationLauncher
import ua.gov.diia.core.util.system.application.ApplicationLauncherImpl
import ua.gov.diia.core.util.system.application.InstalledApplicationInfoProvider
import ua.gov.diia.core.util.system.application.InstalledApplicationInfoProviderImpl

@Module
@InstallIn(ViewModelComponent::class)
interface AppInfoProviderModule {

    @Binds
    fun bindInstalledAppInfoProvider(
        impl: InstalledApplicationInfoProviderImpl
    ): InstalledApplicationInfoProvider

    @Binds
    fun bindApplicationLauncher(
        impl: ApplicationLauncherImpl
    ): ApplicationLauncher
}
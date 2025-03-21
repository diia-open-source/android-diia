package ua.gov.diia.ps_criminal_cert.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import ua.gov.diia.ps_criminal_cert.ui.details.CriminalCertStatusComposeMapper
import ua.gov.diia.ps_criminal_cert.ui.details.CriminalCertStatusComposeMapperImpl

@Module
@InstallIn(ViewModelComponent::class)
interface CriminalCertDelegationModule {

    @Binds
    fun bindStatusMapper(
        impl: CriminalCertStatusComposeMapperImpl
    ): CriminalCertStatusComposeMapper

}
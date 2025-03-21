package ua.gov.diia.opensource.di.feature

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import ua.gov.diia.opensource.helper.PSCriminalCertHelperImpl
import ua.gov.diia.ps_criminal_cert.helper.PSCriminalCertHelper

@Module
@InstallIn(ViewModelComponent::class)
class PSCriminalCertModule {
    @Provides
    fun providePSCriminalCertHelper(): PSCriminalCertHelper {
        return PSCriminalCertHelperImpl()
    }
}
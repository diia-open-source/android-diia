package ua.gov.diia.opensource.di.delegate

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import ua.gov.diia.core.util.delegation.WithPermission
import ua.gov.diia.opensource.util.delegation.DefaultSelfPermissionBehavior

@ObsoleteCoroutinesApi
@ExperimentalCoroutinesApi
@Module
@InstallIn(FragmentComponent::class)
interface FragmentDelegationModule {

    @Binds
    fun bindPermissionDelegate(
        impl: DefaultSelfPermissionBehavior
    ): WithPermission

}
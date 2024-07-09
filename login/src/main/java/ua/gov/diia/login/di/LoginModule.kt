package ua.gov.diia.login.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.Multibinds
import retrofit2.Retrofit
import retrofit2.create
import ua.gov.diia.core.di.data_source.http.UnauthorizedClient
import ua.gov.diia.login.network.ApiLogin
import ua.gov.diia.login.ui.PostLoginAction

@Module
@InstallIn(SingletonComponent::class)
interface LoginModule {

    @Multibinds
    fun providePostLoginActions(): Set<@JvmSuppressWildcards PostLoginAction>

    companion object {


        @Provides
        @UnauthorizedClient
        fun provideApiLogin(
            @UnauthorizedClient retrofit: Retrofit
        ): ApiLogin = retrofit.create()
    }
}
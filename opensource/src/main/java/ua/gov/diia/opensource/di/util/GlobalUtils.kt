package ua.gov.diia.opensource.di.util

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ua.gov.diia.core.util.DiiaDispatcherProvider
import ua.gov.diia.core.util.DispatcherProvider
import ua.gov.diia.core.util.alert.ClientAlertDialogsFactory
import ua.gov.diia.core.util.deeplink.DeepLinkActionFactory
import ua.gov.diia.opensource.util.alert.AndroidClientAlertDialogsFactory
import ua.gov.diia.opensource.util.deeplink.AndroidDeepLinkActionFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
interface GlobalUtils {

    @Binds
    @Singleton
    fun bindTemplateFactory(impl: AndroidClientAlertDialogsFactory): ClientAlertDialogsFactory

    @Binds
    @Singleton
    fun bindCoroutineDispatcher(impl: DiiaDispatcherProvider): DispatcherProvider

    @Binds
    fun bindDeepLinkActionFactory(impl: AndroidDeepLinkActionFactory): DeepLinkActionFactory
}

package ua.gov.diia.opensource.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ua.gov.diia.core.models.ContextMenuField
import ua.gov.diia.core.util.DiiaDispatcherProvider
import ua.gov.diia.core.util.DispatcherProvider
import ua.gov.diia.core.util.alert.ClientAlertDialogsFactory
import ua.gov.diia.core.util.deeplink.DeepLinkActionFactory
import ua.gov.diia.core.util.delegation.WithAppConfig
import ua.gov.diia.core.util.delegation.WithBuildConfig
import ua.gov.diia.core.util.delegation.WithContextMenu
import ua.gov.diia.core.util.delegation.WithDeeplinkHandling
import ua.gov.diia.core.util.delegation.WithErrorHandling
import ua.gov.diia.core.util.delegation.WithErrorHandlingOnFlow
import ua.gov.diia.core.util.delegation.WithPermission
import ua.gov.diia.core.util.delegation.WithPushHandling
import ua.gov.diia.core.util.delegation.WithPushNotification
import ua.gov.diia.core.util.delegation.WithRatingDialog
import ua.gov.diia.core.util.delegation.WithRatingDialogOnFlow
import ua.gov.diia.core.util.delegation.WithRetryLastAction
import ua.gov.diia.documents.ui.WithPdfDocument
import ua.gov.diia.opensource.helper.documents.WithPdfDocumentImpl
import ua.gov.diia.opensource.ui.AndroidClientAlertDialogsFactory
import ua.gov.diia.opensource.util.AndroidDeepLinkActionFactory
import ua.gov.diia.opensource.util.DefaultDeeplinkHandleBehaviour
import ua.gov.diia.opensource.util.DefaultErrorHandlingBehaviour
import ua.gov.diia.opensource.util.DefaultErrorHandlingBehaviourOnFlow
import ua.gov.diia.opensource.util.DefaultPushHandlerBehaviour
import ua.gov.diia.opensource.util.DefaultPushNotificationBehaviour
import ua.gov.diia.opensource.util.DefaultRatingDialogBehaviour
import ua.gov.diia.opensource.util.DefaultRatingDialogBehaviourOnFlow
import ua.gov.diia.opensource.util.DefaultRetryLastActionBehaviour
import ua.gov.diia.opensource.util.DefaultSelfPermissionBehavior
import ua.gov.diia.opensource.util.DefaultWithContextMenuBehaviour
import ua.gov.diia.opensource.util.WithAppConfigImpl
import ua.gov.diia.opensource.util.WithBuildConfigImpl
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
    fun bindWithBuildConfig(impl: WithBuildConfigImpl): WithBuildConfig

    @Binds
    fun bindWithContextMenu(impl: DefaultWithContextMenuBehaviour<ContextMenuField>): WithContextMenu<ContextMenuField>

    @Binds
    fun bindWithPushHandling(impl: DefaultPushHandlerBehaviour): WithPushHandling

    @Binds
    fun bindWithPushNotification(impl: DefaultPushNotificationBehaviour): WithPushNotification

    @Binds
    fun bindWithRatingDialog(impl: DefaultRatingDialogBehaviour): WithRatingDialog

    @Binds
    fun bindWithAppConfig(impl: WithAppConfigImpl): WithAppConfig

    @Binds
    fun bindPermissionDelegate(impl: DefaultSelfPermissionBehavior): WithPermission

    @Binds
    fun bindErrorHandlerDelegateOnFlow(impl: DefaultErrorHandlingBehaviourOnFlow): WithErrorHandlingOnFlow


    @Binds
    fun bindRetryDelegate(impl: DefaultRetryLastActionBehaviour): WithRetryLastAction

    @Binds
    fun bindErrorHandlerDelegate(impl: DefaultErrorHandlingBehaviour): WithErrorHandling

    @Binds
    fun bindDeeplinkHandler(
        impl: DefaultDeeplinkHandleBehaviour
    ): WithDeeplinkHandling

    @Binds
    fun bindDeepLinkActionFactory(impl: AndroidDeepLinkActionFactory): DeepLinkActionFactory

    @Binds
    fun bindRatingDialogDelegateOnFlow(impl: DefaultRatingDialogBehaviourOnFlow): WithRatingDialogOnFlow

    @Binds
    fun bindWithPdfDocument(impl: WithPdfDocumentImpl): WithPdfDocument
}

package ua.gov.diia.opensource.di.delegate

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import ua.gov.diia.core.models.ContextMenuField
import ua.gov.diia.core.util.delegation.*
import ua.gov.diia.opensource.util.delegation.*

@Module
@InstallIn(ViewModelComponent::class)
interface ViewModelDelegationModule {

    @Binds
    fun bindContextMenuDelegate(
        impl: DefaultWithContextMenuBehaviour<ContextMenuField>
    ): WithContextMenu<ContextMenuField>

    @Binds
    fun bindErrorHandlerDelegate(
        impl: DefaultErrorHandlingBehaviour
    ): WithErrorHandling

    @Binds
    fun bindRetryDelegate(
        impl: DefaultRetryLastActionBehaviour
    ): WithRetryLastAction

    @Binds
    fun bindPushNotificationDelegate(
        impl: DefaultPushNotificationBehaviour
    ): WithPushNotification

    @Binds
    fun bindPushHandler(
        impl: DefaultPushHandlerBehaviour
    ): WithPushHandling


    @Binds
    fun bindDeeplinkHandler(
        impl: DefaultDeeplinkHandleBehaviour
    ): WithDeeplinkHandling

    @Binds
    fun bindRatingDialogDelegate(
        impl: DefaultRatingDialogBehaviour): WithRatingDialog

    @Binds
    fun bindRatingDialogDelegateOnFlow(
        impl: DefaultRatingDialogBehaviourOnFlow): WithRatingDialogOnFlow


    @Binds
    fun bindErrorHandlerDelegateOnFlow(
        impl: DefaultErrorHandlingBehaviourOnFlow
    ): WithErrorHandlingOnFlow

    @Binds
    fun bindPermissionDelegate(
        impl: DefaultSelfPermissionBehavior
    ): WithPermission
}
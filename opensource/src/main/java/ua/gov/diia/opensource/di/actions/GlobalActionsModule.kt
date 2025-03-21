package ua.gov.diia.opensource.di.actions

import android.content.Context
import androidx.lifecycle.MutableLiveData
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import ua.gov.diia.core.di.actions.GlobalActionAllowAuthorizedLinks
import ua.gov.diia.core.di.actions.GlobalActionConfirmDocumentRemoval
import ua.gov.diia.core.di.actions.GlobalActionDeeplink
import ua.gov.diia.core.di.actions.GlobalActionDeleteDocument
import ua.gov.diia.core.di.actions.GlobalActionDocLoadingIndicator
import ua.gov.diia.core.di.actions.GlobalActionDocumentBackground
import ua.gov.diia.core.di.actions.GlobalActionFocusOnDocument
import ua.gov.diia.core.di.actions.GlobalActionLogout
import ua.gov.diia.core.di.actions.GlobalActionNetworkState
import ua.gov.diia.core.di.actions.GlobalActionNotificationRead
import ua.gov.diia.core.di.actions.GlobalActionNotificationReceived
import ua.gov.diia.core.di.actions.GlobalActionNotificationsPop
import ua.gov.diia.core.di.actions.GlobalActionSelectedMenuItem
import ua.gov.diia.core.di.actions.GlobalActionUpdateLocalDocument
import ua.gov.diia.core.models.deeplink.DeepLinkAction
import ua.gov.diia.core.models.dialogs.TemplateDialogModel
import ua.gov.diia.core.models.document.DiiaDocument
import ua.gov.diia.core.network.connectivity.ConnectivityObserver
import ua.gov.diia.core.util.event.UiDataEvent
import ua.gov.diia.core.util.event.UiEvent
import ua.gov.diia.documents.di.GlobalActionUpdateDocument
import ua.gov.diia.home.model.background.DocumentBackground
import ua.gov.diia.opensource.data.data_source.network.connectivity.NetworkConnectivityObserver
import ua.gov.diia.ui_base.models.homescreen.HomeMenuItemConstructor
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object GlobalActionsModule {

    @Provides
    @GlobalActionLogout
    @Singleton
    fun provideActionLogout() = MutableLiveData<UiEvent>()

    @Provides
    @GlobalActionProlongUser
    @Singleton
    fun provideActionUserVerification() = MutableLiveData<UiDataEvent<TemplateDialogModel>>()

    @Provides
    @GlobalActionNotificationRead
    @Singleton
    fun provideActionNotificationRead() = MutableLiveData<UiDataEvent<String>>()

    @Provides
    @GlobalActionNotificationReceived
    @Singleton
    fun provideActionNotificationReceived() = MutableLiveData<UiEvent>()

    @Provides
    @GlobalActionNotificationsPop
    @Singleton
    fun provideActionNotificationsPop() = MutableLiveData<UiEvent>()

    @Provides
    @Singleton
    @GlobalActionNetworkState
    fun provideNetworkStateFlow(
        @ApplicationContext context: Context
    ): ConnectivityObserver {
        return NetworkConnectivityObserver(context)
    }

    @Provides
    @GlobalActionDocLoadingIndicator
    @Singleton
    fun provideActionDocLoadingIndicator() = MutableSharedFlow<UiDataEvent<Boolean>>()

    @Provides
    @GlobalActionDeeplink
    @Singleton
    fun provideActionDeeplink() = MutableStateFlow<UiDataEvent<DeepLinkAction>?>(null)

    @Provides
    @Singleton
    @GlobalActionAllowAuthorizedLinks
    fun provideActionAllowAuthorizedLinks() = MutableSharedFlow<UiDataEvent<Boolean>>()

    @Provides
    @GlobalActionForceAppUpdate
    @Singleton
    fun provideActionForceUpdate() = MutableStateFlow(UiDataEvent(false))

    @Provides
    @GlobalActionDocumentBackground
    @Singleton
    fun provideActionDocumentBackground() = MutableStateFlow<UiDataEvent<DocumentBackground>?>(null)

    @Provides
    @GlobalActionUpdateDocument
    @Singleton
    fun provideActionUpdateDocument() = MutableStateFlow<UiDataEvent<DiiaDocument>?>(null)


    @Provides
    @GlobalActionConfirmDocumentRemoval
    @Singleton
    fun provideActionConfirmDocumentRemoval() = MutableStateFlow<UiDataEvent<String>?>(null)

    @Provides
    @GlobalActionFocusOnDocument
    @Singleton
    fun provideActionFocusOnDoc() = MutableStateFlow<UiDataEvent<String>?>(null)

    @Provides
    @GlobalActionDeleteDocument
    @Singleton
    fun provideActionDeleteDoc() = MutableStateFlow<UiDataEvent<String>?>(null)

    @Provides
    @GlobalActionUpdateLocalDocument
    @Singleton
    fun provideActionUpdateLocalDoc() = MutableStateFlow<UiDataEvent<String>?>(null)

    @Provides
    @GlobalActionSelectedMenuItem
    @Singleton
    fun provideActionSelectedMenuItem() =
        MutableStateFlow<UiDataEvent<HomeMenuItemConstructor>?>(null)
}
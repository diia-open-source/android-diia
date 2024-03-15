package ua.gov.diia.opensource.di

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.ElementsIntoSet
import dagger.multibindings.IntoSet
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import retrofit2.Retrofit
import retrofit2.create
import ua.gov.diia.core.controller.PromoController
import ua.gov.diia.core.data.repository.DataRepository
import ua.gov.diia.core.data.repository.SystemRepository
import ua.gov.diia.core.di.actions.GlobalActionAllowAuthorizedLinks
import ua.gov.diia.core.di.actions.GlobalActionConfirmDocumentRemoval
import ua.gov.diia.core.di.actions.GlobalActionDeeplink
import ua.gov.diia.core.di.actions.GlobalActionDocLoadingIndicator
import ua.gov.diia.core.di.actions.GlobalActionFocusOnDocument
import ua.gov.diia.core.di.actions.GlobalActionLazy
import ua.gov.diia.core.di.actions.GlobalActionLogout
import ua.gov.diia.core.di.actions.GlobalActionNetworkState
import ua.gov.diia.core.di.actions.GlobalActionNotificationRead
import ua.gov.diia.core.di.actions.GlobalActionNotificationReceived
import ua.gov.diia.core.di.actions.GlobalActionSelectedMenuItem
import ua.gov.diia.core.di.data_source.http.UnauthorizedClient
import ua.gov.diia.core.models.ActionDataLazy
import ua.gov.diia.core.models.deeplink.DeepLinkAction
import ua.gov.diia.core.models.dialogs.TemplateDialogModel
import ua.gov.diia.core.network.apis.ApiAuth
import ua.gov.diia.core.network.connectivity.ConnectivityObserver
import ua.gov.diia.core.util.event.UiDataEvent
import ua.gov.diia.core.util.event.UiEvent
import ua.gov.diia.core.util.settings_action.SettingsActionExecutor
import ua.gov.diia.diia_storage.DiiaStorage
import ua.gov.diia.diia_storage.PreferenceConfiguration
import ua.gov.diia.diia_storage.SecureDiiaStorage
import ua.gov.diia.diia_storage.store.Preferences
import ua.gov.diia.diia_storage.store.repository.authorization.AuthorizationRepository
import ua.gov.diia.diia_storage.store.repository.authorization.AuthorizationRepositoryImpl
import ua.gov.diia.diia_storage.store.repository.system.SystemRepositoryImpl
import ua.gov.diia.login.ui.PostLoginAction
import ua.gov.diia.notifications.store.NotificationsPreferences
import ua.gov.diia.notifications.util.settings_action.PushTokenUpdateActionExecutor
import ua.gov.diia.opensource.data.network.NetworkConnectivityObserver
import ua.gov.diia.opensource.repository.ps.PublicServiceDataRepository
import ua.gov.diia.opensource.repository.settings.AppSettingsRepository
import ua.gov.diia.opensource.repository.settings.AppSettingsRepositoryImpl
import ua.gov.diia.opensource.ui.PromoControllerImpl
import ua.gov.diia.publicservice.di.DataRepositoryPublicServiceCategories
import ua.gov.diia.publicservice.models.PublicServicesCategories
import ua.gov.diia.ui_base.models.homescreen.HomeMenuItemConstructor
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface AppModule {

    @Binds
    fun bindAppSettingsRepository(impl: AppSettingsRepositoryImpl): AppSettingsRepository

    @Binds
    fun bindSettingsRepository(impl: AuthorizationRepositoryImpl): AuthorizationRepository

    @Binds
    fun bindSystemRepository(impl: SystemRepositoryImpl): SystemRepository

    @Binds
    fun bindPromoController(impl: PromoControllerImpl): PromoController

    @Binds
    @Singleton
    @DataRepositoryPublicServiceCategories
    fun bindPublicServiceCategoriesRepository(
        impl: PublicServiceDataRepository
    ): DataRepository<@JvmSuppressWildcards PublicServicesCategories?>

    @Binds
    @IntoSet
    fun bindPostLoginAction(
        impl: PublicServiceDataRepository
    ): PostLoginAction

    companion object {

        @Provides
        @GlobalActionConfirmDocumentRemoval
        @Singleton
        fun provideActionConfirmDocumentRemoval() = MutableStateFlow<UiDataEvent<String>?>(null)

        @Provides
        @GlobalActionFocusOnDocument
        @Singleton
        fun provideActionFocusOnDoc() = MutableStateFlow<UiDataEvent<String>?>(null)

        @Provides
        @GlobalActionDocLoadingIndicator
        @Singleton
        fun provideActionDocLoadingIndicator() = MutableSharedFlow<UiDataEvent<Boolean>>()

        @Provides
        @Singleton
        @GlobalActionAllowAuthorizedLinks
        fun provideActionAllowAuthorizedLinks() = MutableSharedFlow<UiDataEvent<Boolean>>()

        @Provides
        @GlobalActionSelectedMenuItem
        @Singleton
        fun provideActionSelectedMenuItem() = MutableStateFlow<UiDataEvent<HomeMenuItemConstructor>?>(null)

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
        @Singleton
        @GlobalActionNetworkState
        fun provideNetworkStateFlow(
            @ApplicationContext context: Context
        ): ConnectivityObserver {
            return NetworkConnectivityObserver(context)
        }

        @Provides
        @UnauthorizedClient
        fun provideApiAuth(
            @UnauthorizedClient retrofit: Retrofit
        ): ApiAuth = retrofit.create()

        @Provides
        @Singleton
        @GlobalActionLazy
        fun provideActionLazy() = MutableSharedFlow<UiDataEvent<ActionDataLazy>>(
            replay = 1,
            onBufferOverflow = BufferOverflow.DROP_LATEST
        )

        @Provides
        @GlobalActionDeeplink
        @Singleton
        fun provideActionDeeplink() = MutableStateFlow<UiDataEvent<DeepLinkAction>?>(null)

        @Provides
        @GlobalActionNotificationReceived
        @Singleton
        fun provideActionNotificationReceived() = MutableLiveData<UiEvent>()

        @Provides
        @MoshiAdapterPublicServiceCategories
        fun provideAdapterPublicServiceCategory(): JsonAdapter<PublicServicesCategories> =
            Moshi.Builder()
                .add(KotlinJsonAdapterFactory())
                .build()
                .adapter(PublicServicesCategories::class.java)

        @Provides
        @Singleton
        fun provideDiiaStorage(
            @ApplicationContext context: Context
        ): DiiaStorage {
            val preferenceConfiguration = PreferenceConfiguration(
                _preferenceName = Preferences.Settings.NAME_DIIA,
                preferenceNamePrefix = "",
                allowedScopes = setOf(
                    Preferences.Scopes.AUTH_SCOPE,
                    Preferences.Scopes.UPDATE_SCOPE,
                    Preferences.Scopes.PIN_SCOPE,
                    NotificationsPreferences.Scopes.PUSH_SCOPE,
                    Preferences.Scopes.USER_SCOPE,
                    Preferences.Scopes.DOUBLE_CHECK,
                    NotificationsPreferences.Scopes.NOTIFICATION,
                    Preferences.Scopes.FAQS,
                    Preferences.Scopes.FEATURES,
                    Preferences.Scopes.USER_PREFERENCES,
                    Preferences.Scopes.INVINCIBILITY_PREFERENCES,
                )
            )
            return SecureDiiaStorage(context, preferenceConfiguration)
        }

        @Provides
        @ElementsIntoSet
        fun provideSettingsAction(
            pushTokenUpdateAction: PushTokenUpdateActionExecutor,
        ): Set<SettingsActionExecutor> {
            return setOf(pushTokenUpdateAction)
        }
    }
}
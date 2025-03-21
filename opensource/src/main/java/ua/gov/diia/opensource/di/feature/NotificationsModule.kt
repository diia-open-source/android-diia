package ua.gov.diia.opensource.di.feature

import android.content.Context
import androidx.lifecycle.MutableLiveData
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.MutableStateFlow
import ua.gov.diia.core.di.actions.GlobalActionFocusOnDocument
import ua.gov.diia.core.di.actions.GlobalActionNotificationsPop
import ua.gov.diia.core.di.actions.GlobalActionSelectedMenuItem
import ua.gov.diia.core.push.BasePushNotificationAction
import ua.gov.diia.core.util.delegation.WithCrashlytics
import ua.gov.diia.core.util.event.UiDataEvent
import ua.gov.diia.core.util.event.UiEvent
import ua.gov.diia.notifications.helper.NotificationHelper
import ua.gov.diia.opensource.data.data_source.network.ApiLogger
import ua.gov.diia.opensource.helper.NotificationHelperImpl
import ua.gov.diia.opensource.data.repository.settings.AppSettingsRepository
import ua.gov.diia.ui_base.models.homescreen.HomeMenuItemConstructor

@Module
@InstallIn(SingletonComponent::class)
class NotificationsModule {

    @Provides
    fun provideNotificationHelper(
        @GlobalActionFocusOnDocument globalActionFocusOnDocument: MutableStateFlow<UiDataEvent<String>?>,
        @GlobalActionSelectedMenuItem globalActionSelectedMenuItem: MutableStateFlow<UiDataEvent<HomeMenuItemConstructor>?>,
        @GlobalActionNotificationsPop actionNotificationsPop: MutableLiveData<UiEvent>,
        withCrashlytics: WithCrashlytics,
        actions: List<@JvmSuppressWildcards BasePushNotificationAction>,
        appSettingsRepository: AppSettingsRepository,
        @ApplicationContext context: Context,
        logger: ApiLogger,
    ): NotificationHelper {
        return NotificationHelperImpl(
            globalActionFocusOnDocument,
            globalActionSelectedMenuItem,
            actionNotificationsPop,
            withCrashlytics,
            actions,
            appSettingsRepository,
            context,
            logger,
        )
    }
}
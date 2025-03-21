package ua.gov.diia.opensource.di.feature

import androidx.work.WorkManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.MutableStateFlow
import ua.gov.diia.core.controller.DeeplinkProcessor
import ua.gov.diia.core.controller.NotificationController
import ua.gov.diia.core.di.actions.GlobalActionFocusOnDocument
import ua.gov.diia.core.di.actions.GlobalActionSelectedMenuItem
import ua.gov.diia.core.push.BasePushNotificationAction
import ua.gov.diia.core.util.event.UiDataEvent
import ua.gov.diia.diia_storage.DiiaStorage
import ua.gov.diia.home.helper.HomeHelper
import ua.gov.diia.notifications.NotificationControllerImpl
import ua.gov.diia.notifications.helper.NotificationHelper
import ua.gov.diia.notifications.store.datasource.notifications.KeyValueNotificationDataSource
import ua.gov.diia.notifications.store.datasource.notifications.NotificationDataRepository
import ua.gov.diia.notifications.util.notification.manager.DiiaNotificationManager
import ua.gov.diia.notifications.util.notification.push.PushTokenProvider
import ua.gov.diia.opensource.deeplinkprocessor.DeepLinkActionStartFlowProcessor
import ua.gov.diia.opensource.deeplinkprocessor.DeepLinkActionViewDocumentProcessor
import ua.gov.diia.opensource.deeplinkprocessor.DeeplinkProcessorImpl
import ua.gov.diia.opensource.helper.HomeHelperImpl
import ua.gov.diia.ui_base.models.homescreen.HomeMenuItemConstructor

@Module
@InstallIn(SingletonComponent::class)
class HomeModule {

    @Provides
    fun provideDeepLinkProcessor(
        notificationController: NotificationController,
        @GlobalActionFocusOnDocument globalActionFocusOnDocument: MutableStateFlow<UiDataEvent<String>?>,
        @GlobalActionSelectedMenuItem globalActionSelectedMenuItem: MutableStateFlow<UiDataEvent<HomeMenuItemConstructor>?>,
        keyValueStore: DiiaStorage,
        actions: List<@JvmSuppressWildcards BasePushNotificationAction>,

        ): DeeplinkProcessor {
        val linkActionProcessors = listOf(
            DeepLinkActionViewDocumentProcessor(
                globalActionFocusOnDocument,
                globalActionSelectedMenuItem
            ),
            DeepLinkActionStartFlowProcessor(
                notificationController
            )
        )

        return DeeplinkProcessorImpl(linkActionProcessors)
    }

    @Provides
    fun provideNotificationController(
        workManager: WorkManager,
        notificationsDataSource: NotificationDataRepository,
        notificationManager: DiiaNotificationManager,
        pushTokenProvider: PushTokenProvider,
        keyValueSource: KeyValueNotificationDataSource,
        notificationHelper: NotificationHelper
    ): NotificationController {
        return NotificationControllerImpl(
            workManager,
            notificationsDataSource,
            notificationManager,
            pushTokenProvider,
            keyValueSource,
            notificationHelper
        )
    }

    @Provides
    fun provideHomeHelper(): HomeHelper {
        return HomeHelperImpl()
    }
}
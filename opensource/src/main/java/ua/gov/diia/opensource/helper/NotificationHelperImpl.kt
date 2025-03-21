package ua.gov.diia.opensource.helper

import android.content.Context
import android.content.Intent
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavDirections
import kotlinx.coroutines.flow.MutableStateFlow
import ua.gov.diia.core.di.actions.GlobalActionFocusOnDocument
import ua.gov.diia.core.di.actions.GlobalActionNotificationsPop
import ua.gov.diia.core.di.actions.GlobalActionSelectedMenuItem
import ua.gov.diia.core.models.notification.pull.PullNotificationItemSelection
import ua.gov.diia.core.models.notification.push.PushNotification
import ua.gov.diia.core.push.BasePushNotificationAction
import ua.gov.diia.core.util.delegation.WithCrashlytics
import ua.gov.diia.core.util.event.UiDataEvent
import ua.gov.diia.core.util.event.UiEvent
import ua.gov.diia.home.model.HomeMenuItem
import ua.gov.diia.notifications.helper.NotificationHelper
import ua.gov.diia.notifications.models.notification.push.DiiaNotificationChannel
import ua.gov.diia.opensource.R
import ua.gov.diia.opensource.VendorActivity
import ua.gov.diia.opensource.data.data_source.network.ApiLogger
import ua.gov.diia.opensource.data.repository.settings.AppSettingsRepository
import ua.gov.diia.opensource.util.extensions.fragment.getPullNotificationDirection
import ua.gov.diia.ui_base.models.homescreen.HomeMenuItemConstructor

class NotificationHelperImpl(
    @GlobalActionFocusOnDocument val globalActionFocusOnDocument: MutableStateFlow<UiDataEvent<String>?>,
    @GlobalActionSelectedMenuItem val globalActionSelectedMenuItem: MutableStateFlow<UiDataEvent<HomeMenuItemConstructor>?>,
    @GlobalActionNotificationsPop val actionNotificationsPop: MutableLiveData<UiEvent>,
    val withCrashlytics: WithCrashlytics,
    private val actions: List<@JvmSuppressWildcards BasePushNotificationAction>,
    private val appSettingsRepository: AppSettingsRepository,
    private val context: Context,
    private val logger: ApiLogger,
) : NotificationHelper {


    private suspend fun focusOnDocument(docType: String, shouldPop: Boolean) {
        if (shouldPop) {
            actionNotificationsPop.postValue(UiEvent())
        }
        globalActionFocusOnDocument.emit(UiDataEvent(docType))
        globalActionSelectedMenuItem.emit(UiDataEvent(HomeMenuItem.DOCUMENTS))
    }

    override fun isMessageNotification(resourceType: String): Boolean {
        return false
    }

    override suspend fun getHomeDestinationId() = R.id.homeF

    override suspend fun navigateToDocument(
        item: PullNotificationItemSelection,
        shouldPop: Boolean
    ): NavDirections? {
        return getPullNotificationDirection(item, actions)
    }

    override suspend fun getLastDocumentUpdate(): String? =
        appSettingsRepository.getLastDocumentUpdate()

    override suspend fun getLastActiveDate(): String? = appSettingsRepository.getLastActiveDate()

    override fun getMainActivityIntent(): Intent {
        return Intent(context, VendorActivity::class.java)
    }

    override fun log(data: String) {
        logger.log(data)
    }

    override fun getNotificationChannel(notif: PushNotification): String {
        return when (notif.action.type) {
            else -> DiiaNotificationChannel.DEFAULT.id
        }
    }
}
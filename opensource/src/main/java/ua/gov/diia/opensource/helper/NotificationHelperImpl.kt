package ua.gov.diia.opensource.helper

import android.content.Context
import android.content.Intent
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavDirections
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import ua.gov.diia.core.di.actions.GlobalActionFocusOnDocument
import ua.gov.diia.core.di.actions.GlobalActionNotificationsPop
import ua.gov.diia.core.di.actions.GlobalActionSelectedMenuItem
import ua.gov.diia.core.models.notification.pull.PullNotificationItemSelection
import ua.gov.diia.core.models.notification.push.PushNotification
import ua.gov.diia.core.util.delegation.WithCrashlytics
import ua.gov.diia.core.util.event.UiDataEvent
import ua.gov.diia.core.util.event.UiEvent
import ua.gov.diia.home.model.HomeMenuItem
import ua.gov.diia.notifications.helper.NotificationHelper
import ua.gov.diia.notifications.models.notification.push.DiiaNotificationChannel
import ua.gov.diia.opensource.R
import ua.gov.diia.opensource.model.notification.PushNotificationActionType
import ua.gov.diia.opensource.repository.settings.AppSettingsRepository
import ua.gov.diia.opensource.ui.activities.MainActivity
import ua.gov.diia.ui_base.models.homescreen.HomeMenuItemConstructor
import javax.inject.Inject

class NotificationHelperImpl @Inject constructor(
    @GlobalActionFocusOnDocument val globalActionFocusOnDocument: MutableStateFlow<UiDataEvent<String>?>,
    @GlobalActionSelectedMenuItem val globalActionSelectedMenuItem: MutableStateFlow<UiDataEvent<HomeMenuItemConstructor>?>,
    @GlobalActionNotificationsPop val actionNotificationsPop: MutableLiveData<UiEvent>,
    val withCrashlytics: WithCrashlytics,
    @ApplicationContext private val context: Context,
    private val appSettingsRepository: AppSettingsRepository
) : NotificationHelper {

    private suspend fun focusOnDocument(docType: String, shouldPop: Boolean) {
        if(shouldPop) {
            actionNotificationsPop.postValue(UiEvent())
        }
        globalActionFocusOnDocument.emit(UiDataEvent(docType))
        globalActionSelectedMenuItem.emit(UiDataEvent(HomeMenuItem.DOCUMENTS))
    }

    override fun isMessageNotification(resourceType: String): Boolean {
        return PushNotificationActionType.MESSAGE == PushNotificationActionType.fromId(resourceType)
    }

    override suspend fun navigateToDocument(
        item: PullNotificationItemSelection,
        shouldPop: Boolean
    ): NavDirections? {
        if (isViewDocType(item.resourceType)) {
            val itemDocName = getDocName(item.resourceType)

            if (itemDocName == null) {
                withCrashlytics.sendNonFatalError(IllegalStateException("This notification isn't a DOCUMENT type, it is:${item.resourceType} type"))
                return null
            }
            focusOnDocument(itemDocName, shouldPop)
            return null
        } else {
            return null // TODO getPullNotificationDirection(item)
        }
    }

    override suspend fun getHomeDestinationId() = R.id.homeF

    override suspend fun getLastDocumentUpdate(): String? =
        appSettingsRepository.getLastDocumentUpdate()

    override suspend fun getLastActiveDate(): String? = appSettingsRepository.getLastActiveDate()

    override fun getMainActivityIntent(): Intent {
        return Intent(context, MainActivity::class.java)
    }

    private fun getDocName(resourceType: String): String? {
        return if (resourceType.startsWith(PushNotificationActionType.DOCUMENT_VIEW.id)) {
            resourceType.removePrefix(PushNotificationActionType.DOCUMENT_VIEW.id)
        } else {
            null
        }
    }

    private fun isViewDocType(resourceType: String): Boolean {
        return resourceType.startsWith(PushNotificationActionType.DOCUMENT_VIEW.id)
    }

    override fun log(data: String) {
        // Implement logging data
    }

    override fun getNotificationChannel(notif: PushNotification): String {
        return when (notif.action.type) {
            else -> DiiaNotificationChannel.DEFAULT.id
        }
    }
}

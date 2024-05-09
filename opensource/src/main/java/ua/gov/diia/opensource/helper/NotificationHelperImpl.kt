package ua.gov.diia.opensource.helper

import android.content.Context
import android.content.Intent
import androidx.navigation.NavDirections
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import ua.gov.diia.core.di.actions.GlobalActionFocusOnDocument
import ua.gov.diia.core.di.actions.GlobalActionSelectedMenuItem
import ua.gov.diia.core.models.notification.pull.PullNotificationItemSelection
import ua.gov.diia.core.models.notification.push.PushNotification
import ua.gov.diia.core.util.delegation.WithCrashlytics
import ua.gov.diia.core.util.event.UiDataEvent
import ua.gov.diia.home.model.HomeMenuItem
import ua.gov.diia.notifications.helper.NotificationHelper
import ua.gov.diia.notifications.models.notification.push.DiiaNotificationChannel
import ua.gov.diia.opensource.model.notification.PushNotificationActionType
import ua.gov.diia.opensource.repository.settings.AppSettingsRepository
import ua.gov.diia.opensource.ui.activities.MainActivity
import ua.gov.diia.ui_base.models.homescreen.HomeMenuItemConstructor
import javax.inject.Inject

class NotificationHelperImpl @Inject constructor(
    @GlobalActionFocusOnDocument private val globalActionFocusOnDocument: MutableStateFlow<UiDataEvent<String>?>,
    @GlobalActionSelectedMenuItem private val globalActionSelectedMenuItem: MutableStateFlow<UiDataEvent<HomeMenuItemConstructor>?>,
    private val withCrashlytics: WithCrashlytics,
    @ApplicationContext private val context: Context,
    private val appSettingsRepository: AppSettingsRepository
) : NotificationHelper {

    override fun isMessageNotification(resourceType: String) =
        PushNotificationActionType.MESSAGE == PushNotificationActionType.fromId(resourceType)

    override suspend fun navigateToDocument(item: PullNotificationItemSelection): NavDirections? {
        if (isViewDocType(item.resourceType)) {
            val itemDocName = getDocName(item.resourceType)
            if (itemDocName == null) {
                withCrashlytics.sendNonFatalError(IllegalStateException("This notification isn't a DOCUMENT type, it is:${item.resourceType} type"))
                return null
            }
            focusOnDocument(itemDocName)
            return null
        }
        return null
    }

    override suspend fun getLastDocumentUpdate() = appSettingsRepository.getLastDocumentUpdate()

    override suspend fun getLastActiveDate() = appSettingsRepository.getLastActiveDate()

    override fun getMainActivityIntent() = Intent(context, MainActivity::class.java)

    override fun log(data: String) {
        // Implement logging data
    }

    override fun getNotificationChannel(notif: PushNotification) =
        when (notif.action.type) {
            else -> DiiaNotificationChannel.DEFAULT.id
        }

    private suspend fun focusOnDocument(docType: String) {
        globalActionFocusOnDocument.emit(UiDataEvent(docType))
        globalActionSelectedMenuItem.emit(UiDataEvent(HomeMenuItem.DOCUMENTS))
    }

    private fun getDocName(resourceType: String) =
        if (resourceType.startsWith(PushNotificationActionType.DOCUMENT_VIEW.id)) {
            resourceType.removePrefix(PushNotificationActionType.DOCUMENT_VIEW.id)
        } else {
            null
        }

    private fun isViewDocType(resourceType: String) =
        resourceType.startsWith(PushNotificationActionType.DOCUMENT_VIEW.id)
}

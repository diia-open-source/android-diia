package ua.gov.diia.notifications

import android.os.Build
import androidx.work.WorkManager
import ua.gov.diia.core.controller.NotificationController
import ua.gov.diia.diia_storage.store.Preferences
import ua.gov.diia.notifications.store.datasource.notifications.KeyValueNotificationDataSource
import ua.gov.diia.notifications.store.datasource.notifications.NotificationDataRepository
import ua.gov.diia.notifications.util.notification.manager.DiiaNotificationManager
import ua.gov.diia.notifications.util.notification.push.PushTokenProvider
import ua.gov.diia.notifications.work.SendPushTokenWork

class NotificationControllerImpl(
    private val workManager: WorkManager,
    private val notificationsDataSource: NotificationDataRepository,
    private val notificationManager: DiiaNotificationManager,
    private val pushTokenProvider: PushTokenProvider,
    private val keyValueSource: KeyValueNotificationDataSource,
): NotificationController {

    override suspend fun markAsRead(resId: String?) {
        if (resId != null && resId != Preferences.DEF) {
            notificationsDataSource.markNotificationAsRead(resId)
        }
    }

    override fun invalidateNotificationDataSource() {
        notificationsDataSource.invalidate()
    }

    override suspend fun getNotificationsInitial() {
        notificationsDataSource.loadDataFromNetwork(0, 5) {}

    }

    override fun checkPushTokenInSync() {
        val pushTokenSynced: Boolean = keyValueSource.isPushTokenSynced()
        if (!pushTokenSynced) {
            val token = pushTokenProvider.requestCurrentPushToken()
            if (token.isNotEmpty()) {
                SendPushTokenWork.enqueue(workManager, token)
            }
        }
    }

    override suspend fun collectUnreadNotificationCounts(callback: (amount: Int) -> Unit) {
        notificationsDataSource.unreadCount.collect {
            notificationManager.setBadeNumber(it)
            callback(it)
        }
    }

    override suspend fun allowNotifications() {
        keyValueSource.allowNotifications()
    }

    override fun denyNotifications() {
        keyValueSource.denyNotifications()
    }

    override suspend fun checkNotificationsRequested(): Boolean? {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            return keyValueSource.isNotificationRequested()
        } else {
            allowNotifications()
        }
        return null
    }
}
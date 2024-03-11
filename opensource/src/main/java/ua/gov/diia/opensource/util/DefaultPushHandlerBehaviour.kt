package ua.gov.diia.opensource.util

import kotlinx.coroutines.flow.MutableSharedFlow
import ua.gov.diia.core.models.notification.pull.PullNotificationItemSelection
import ua.gov.diia.core.util.delegation.WithCrashlytics
import ua.gov.diia.core.util.delegation.WithPushHandling
import ua.gov.diia.notifications.store.datasource.notifications.NotificationDataRepository
import javax.inject.Inject

class DefaultPushHandlerBehaviour @Inject constructor(
    private val notificationDataSource: NotificationDataRepository,
    private val crashlytics: WithCrashlytics,
) : WithPushHandling {

    private var isConsumed = false

    override val notificationConsumedEvent =
        MutableSharedFlow<PullNotificationItemSelection>()

    override suspend fun consumePush(notification: PullNotificationItemSelection) {
        if (!isConsumed) {
            isConsumed = true
            markNotificationAsRead(notification)
        }
    }

    private suspend fun markNotificationAsRead(notification: PullNotificationItemSelection) {
        try {
            if (notification.notificationId != null) {
                notification.notificationId?.let { notificationId ->
                    notificationDataSource.markNotificationAsRead(notificationId)
                }
            } else {
                if (notification.resourceId != null) {
                    notification.resourceId?.let { resourceId ->
                        val cachedNotification = notificationDataSource
                            .findNotificationByResourceId(resourceId)

                        if (cachedNotification?.isRead == false) {
                            cachedNotification.notificationId?.let {
                                notificationDataSource.markNotificationAsRead(it)
                            }
                        }
                    }

                }
            }

            notificationConsumedEvent.emit(notification)
        } catch (e: Exception) {
            crashlytics.sendNonFatalError(e)
        }
    }
}
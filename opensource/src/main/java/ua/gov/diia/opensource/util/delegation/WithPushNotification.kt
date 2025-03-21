package ua.gov.diia.opensource.util.delegation

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import ua.gov.diia.core.models.ConsumableItem
import ua.gov.diia.core.models.notification.pull.PullNotificationItemSelection
import ua.gov.diia.core.util.delegation.WithCrashlytics
import ua.gov.diia.core.util.delegation.WithPushNotification
import ua.gov.diia.core.util.event.UiDataEvent
import ua.gov.diia.notifications.store.datasource.notifications.NotificationDataRepository
import javax.inject.Inject

class DefaultPushNotificationBehaviour @Inject constructor(
    private val notificationDataSource: NotificationDataRepository,
    private val crashlytics: WithCrashlytics,
) : WithPushNotification {

    private val _notificationConsumedEvent =
        MutableSharedFlow<UiDataEvent<PullNotificationItemSelection>>()
    override val notificationConsumedEvent: Flow<UiDataEvent<PullNotificationItemSelection>> =
        _notificationConsumedEvent

    override suspend fun consumePush(consumablePushItem: ConsumableItem) {
        if (!consumablePushItem.isConsumed && consumablePushItem.item is PullNotificationItemSelection) {
            consumablePushItem.isConsumed = true
            markNotificationAsRead(consumablePushItem.item as PullNotificationItemSelection)
        }
    }

    override suspend fun markNotificationAsRead(resId: String) {
        val cachedNotification =
            notificationDataSource.findNotificationByResourceId(resId)
        if (cachedNotification?.isRead == false) {
            cachedNotification.notificationId?.let {
                notificationDataSource.markNotificationAsRead(it)
            }
        }
    }

    private suspend fun markNotificationAsRead(notification: PullNotificationItemSelection) {
        try {
            notification.notificationId?.let {
                notificationDataSource.markNotificationAsRead(it)
            } ?: notification.resourceId?.let {
                val cachedNotification = notificationDataSource
                    .findNotificationByResourceId(it)

                if (cachedNotification?.isRead == false) {
                    cachedNotification.notificationId?.let { notificationId ->
                        notificationDataSource.markNotificationAsRead(notificationId)
                    }
                }
            }
            _notificationConsumedEvent.emit(UiDataEvent(notification))
        } catch (e: Exception) {
            crashlytics.sendNonFatalError(e)
        }
    }
}
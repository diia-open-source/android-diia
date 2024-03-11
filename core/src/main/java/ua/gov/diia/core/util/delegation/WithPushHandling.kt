package ua.gov.diia.core.util.delegation

import kotlinx.coroutines.flow.Flow
import ua.gov.diia.core.models.notification.pull.PullNotificationItemSelection


interface WithPushHandling {

    val notificationConsumedEvent: Flow<PullNotificationItemSelection>

    suspend fun consumePush(notification: PullNotificationItemSelection)
}

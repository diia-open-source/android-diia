package ua.gov.diia.core.util.delegation

import kotlinx.coroutines.flow.Flow
import ua.gov.diia.core.models.ConsumableItem
import ua.gov.diia.core.models.notification.pull.PullNotificationItemSelection
import ua.gov.diia.core.util.event.UiDataEvent

interface WithPushNotification {

    val notificationConsumedEvent: Flow<UiDataEvent<PullNotificationItemSelection>>

    suspend fun consumePush(consumablePushItem: ConsumableItem)

    suspend fun markNotificationAsRead(resId: String)
}

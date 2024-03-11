package ua.gov.diia.notifications.store.datasource.notifications

import ua.gov.diia.core.di.data_source.http.AuthorizedClient
import ua.gov.diia.notifications.data.data_source.network.api.notification.ApiNotifications
import ua.gov.diia.notifications.models.notification.pull.PullNotificationsResponse
import ua.gov.diia.notifications.models.notification.pull.PullNotificationsToModify
import ua.gov.diia.notifications.models.notification.pull.UpdatePullNotificationResponse
import javax.inject.Inject

class NetworkNotificationDataSource @Inject constructor(
    @AuthorizedClient private val apiNotifications: ApiNotifications
) {

    suspend fun markNotificationsAsRead(notificationsToModify: PullNotificationsToModify): UpdatePullNotificationResponse {
        return apiNotifications.markNotificationsAsRead(notificationsToModify)
    }

    suspend fun deleteNotifications(notificationsToModify: PullNotificationsToModify): UpdatePullNotificationResponse {
        return apiNotifications.deleteNotifications(notificationsToModify)
    }

    suspend fun getNotifications(skip: Int): PullNotificationsResponse {
        return apiNotifications.getNotifications(skip)
    }

}
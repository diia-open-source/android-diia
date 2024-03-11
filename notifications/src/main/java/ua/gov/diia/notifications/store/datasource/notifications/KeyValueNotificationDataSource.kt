package ua.gov.diia.notifications.store.datasource.notifications

import ua.gov.diia.notifications.models.notification.pull.PullNotification

interface KeyValueNotificationDataSource {
    suspend fun fetchData(): List<PullNotification>
    fun fetchUnreadCount(): Int
    fun saveDataToStore(cachedData: List<PullNotification>)
    fun updateUnreadCount(newUnreadCount: Int)

    fun allowNotifications()

    fun isPushTokenSynced(): Boolean

    fun denyNotifications()

    fun isNotificationRequested(): Boolean
    fun setPushToken(token: String)
    fun setIsPushTokenSynced(synced: Boolean)

}
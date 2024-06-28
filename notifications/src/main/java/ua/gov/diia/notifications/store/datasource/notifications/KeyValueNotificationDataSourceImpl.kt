package ua.gov.diia.notifications.store.datasource.notifications

import com.squareup.moshi.JsonAdapter
import ua.gov.diia.core.util.delegation.WithCrashlytics
import ua.gov.diia.diia_storage.DiiaStorage
import ua.gov.diia.diia_storage.model.PreferenceKey
import ua.gov.diia.diia_storage.store.AbstractKeyValueDataSource
import ua.gov.diia.notifications.models.notification.pull.PullNotification
import ua.gov.diia.notifications.store.NotificationsPreferences

class KeyValueNotificationDataSourceImpl constructor(
    keyValueStore: DiiaStorage,
    withCrashlytics: WithCrashlytics,
    override val jsonAdapter: JsonAdapter<List<PullNotification>>
) : AbstractKeyValueDataSource<List<PullNotification>>(keyValueStore, withCrashlytics),
    KeyValueNotificationDataSource {

    override val preferenceKey: PreferenceKey = NotificationsPreferences.NotificationsList

    override suspend fun fetchData(): List<PullNotification> {
        if (store.containsKey(preferenceKey)) {
            return loadData() ?: emptyList()
        }
        return emptyList()
    }

    override fun fetchUnreadCount(): Int {
        return store.getInt(NotificationsPreferences.NotificationsUnreadCount, 0)
    }

    override fun updateUnreadCount(newUnreadCount: Int) {
        store.set(NotificationsPreferences.NotificationsUnreadCount, newUnreadCount)
    }


    override fun allowNotifications() {
        store.set(NotificationsPreferences.AllowNotifications, true)
        store.set(NotificationsPreferences.NotificationsRequested, true)
    }


    override fun isPushTokenSynced(): Boolean =
        store.getBoolean(NotificationsPreferences.IsPushTokenSynced, false)

    override fun denyNotifications() {
        store.set(NotificationsPreferences.NotificationsRequested, true)
    }

    override fun isNotificationRequested(): Boolean =
        store.getBoolean(NotificationsPreferences.NotificationsRequested, false)

    override fun setPushToken(token: String) {
        store.set(NotificationsPreferences.PushToken, token)
    }

    override fun setIsPushTokenSynced(synced: Boolean) {
        store.set(NotificationsPreferences.IsPushTokenSynced, synced)
    }
}
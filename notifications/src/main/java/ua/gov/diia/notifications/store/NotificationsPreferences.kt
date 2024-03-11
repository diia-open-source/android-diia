package ua.gov.diia.notifications.store

import ua.gov.diia.diia_storage.store.Preferences
import ua.gov.diia.diia_storage.model.PreferenceKey

object NotificationsPreferences {

    object Scopes {
        const val PUSH_SCOPE = "push"
        const val NOTIFICATION = "pull_notification"
    }

    open class PushKey(name: String, dataType: Class<*>) :
        PreferenceKey(name, Scopes.PUSH_SCOPE, dataType)
    object PushToken : PushKey("push_token", String::class.java)
    object IsPushTokenSynced : PushKey("is_push_token_synced", Boolean::class.java)

    object AllowNotifications : UserDataKey("allow_notifications", Boolean::class.java)

    object NotificationsList : NotificationKey("notifications_list", String::class.java)

    open class UserDataKey(name: String, dataType: Class<*>) :
        PreferenceKey(name, Preferences.Scopes.USER_SCOPE, dataType)

    open class NotificationKey(name: String, dataType: Class<*>) :
        PreferenceKey(name, Scopes.NOTIFICATION, dataType)
    object NotificationsUnreadCount : NotificationKey("notifications_unread", Int::class.java)

    object NotificationsRequested : Preferences.UserDataKey("notifications_requested", Boolean::class.java)
}
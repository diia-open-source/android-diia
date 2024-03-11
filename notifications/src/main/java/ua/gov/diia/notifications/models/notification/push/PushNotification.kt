package ua.gov.diia.notifications.models.notification.push

import ua.gov.diia.core.models.notification.push.PushNotification

fun PushNotification.getNotificationKey(): Int {
    notificationId?.let {
        return if (it.isNotEmpty()) {
            it.hashCode()
        } else {
            action.let { action -> action.resourceId?.hashCode() ?: action.type.hashCode() }
        }
    }
    return -1
}

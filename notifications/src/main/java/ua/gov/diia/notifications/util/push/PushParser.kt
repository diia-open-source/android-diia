package ua.gov.diia.notifications.util.push

import ua.gov.diia.core.models.notification.push.PushNotification

interface PushParser {

    fun parsePushNotification(pushJson: String): PushNotification?


}
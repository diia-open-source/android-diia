package ua.gov.diia.core.util.deeplink

import ua.gov.diia.core.models.deeplink.*
import ua.gov.diia.core.models.notification.push.PushNotification

interface DeepLinkActionFactory {

    fun buildDeepLinkAction(path: String): DeepLinkAction

    fun buildPathFromPushNotification(pushNotification: PushNotification): String

}

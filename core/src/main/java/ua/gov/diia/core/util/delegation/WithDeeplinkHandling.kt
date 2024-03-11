package ua.gov.diia.core.util.delegation

import kotlinx.coroutines.flow.StateFlow
import ua.gov.diia.core.models.deeplink.DeepLinkAction
import ua.gov.diia.core.models.notification.push.PushNotification
import ua.gov.diia.core.util.event.UiDataEvent


interface WithDeeplinkHandling {

    val deeplinkFlow: StateFlow<UiDataEvent<DeepLinkAction>?>

    suspend fun emitDeeplink(event: UiDataEvent<DeepLinkAction>)

    fun buildDeepLinkAction(path: String): DeepLinkAction

    fun buildPathFromPushNotification(notification: PushNotification): String
}

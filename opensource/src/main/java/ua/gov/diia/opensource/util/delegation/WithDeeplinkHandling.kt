package ua.gov.diia.opensource.util.delegation

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import ua.gov.diia.core.di.actions.GlobalActionDeeplink
import ua.gov.diia.core.models.deeplink.DeepLinkAction
import ua.gov.diia.core.models.notification.push.PushNotification
import ua.gov.diia.core.util.deeplink.DeepLinkActionFactory
import ua.gov.diia.core.util.delegation.WithDeeplinkHandling
import ua.gov.diia.core.util.event.UiDataEvent
import javax.inject.Inject

class DefaultDeeplinkHandleBehaviour @Inject constructor(
    @GlobalActionDeeplink private val globalActionDeeplink: MutableStateFlow<UiDataEvent<DeepLinkAction>?>,
    private val deepLinkActionFactory: DeepLinkActionFactory,
) : WithDeeplinkHandling {

    override val deeplinkFlow: StateFlow<UiDataEvent<DeepLinkAction>?>
        get() = globalActionDeeplink

    override suspend fun emitDeeplink(event: UiDataEvent<DeepLinkAction>) {
        globalActionDeeplink.emit(event)
    }

    override fun buildDeepLinkAction(path: String): DeepLinkAction {
        return deepLinkActionFactory.buildDeepLinkAction(path)
    }

    override fun buildPathFromPushNotification(notification: PushNotification): String {
        return deepLinkActionFactory.buildPathFromPushNotification(notification)
    }
}
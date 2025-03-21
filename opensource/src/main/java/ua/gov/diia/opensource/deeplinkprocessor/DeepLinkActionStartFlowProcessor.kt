package ua.gov.diia.opensource.deeplinkprocessor


import androidx.navigation.NavDirections
import ua.gov.diia.core.controller.NotificationController
import ua.gov.diia.core.models.SingleDeeplinkProcessor
import ua.gov.diia.core.models.deeplink.DeepLinkAction
import ua.gov.diia.core.models.deeplink.DeepLinkActionStartFlow

class DeepLinkActionStartFlowProcessor(
    private val notificationController: NotificationController,
) : SingleDeeplinkProcessor {
    override suspend fun handleDeepLinkAction(linkAction: DeepLinkAction): NavDirections? {
        val action = linkAction as DeepLinkActionStartFlow
        val resId = action.resId
        val resType = action.resType
        notificationController.markAsRead(resId)

        return when (action.flowId) {
            DEEP_LINK_HOME -> null
            else -> null
        }
    }
    override fun isHandled(action: DeepLinkAction): Boolean = action is DeepLinkActionStartFlow

    private companion object {
        const val DEEP_LINK_HOME = "/home/"
    }
}
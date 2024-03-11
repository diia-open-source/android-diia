package ua.gov.diia.opensource.util

import androidx.navigation.NavDirections
import ua.gov.diia.core.controller.DeeplinkProcessor
import ua.gov.diia.core.models.SingleDeeplinkProcessor
import ua.gov.diia.core.models.deeplink.DeepLinkAction

class DeeplinkProcessorImpl(
    private val linkActionProcessors: List<SingleDeeplinkProcessor>
): DeeplinkProcessor {

    override suspend fun handleDeepLinkAction(action: DeepLinkAction): NavDirections? {
        linkActionProcessors.forEach {
            if (it.isHandled(action)) {
                return it.handleDeepLinkAction(action)
            }
        }
        return null
    }
}
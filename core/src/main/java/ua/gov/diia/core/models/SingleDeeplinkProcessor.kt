package ua.gov.diia.core.models

import androidx.navigation.NavDirections
import ua.gov.diia.core.models.deeplink.DeepLinkAction

interface SingleDeeplinkProcessor {
    fun isHandled(action: DeepLinkAction): Boolean
    suspend fun handleDeepLinkAction(action: DeepLinkAction): NavDirections?
}
package ua.gov.diia.core.controller

import androidx.navigation.NavDirections
import ua.gov.diia.core.models.deeplink.DeepLinkAction

interface DeeplinkProcessor {
    /**
     * Handle deeplink action from list of available action list
     */
    suspend fun handleDeepLinkAction(action: DeepLinkAction): NavDirections?
}
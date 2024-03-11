package ua.gov.diia.pin.helper

import androidx.fragment.app.Fragment
import ua.gov.diia.pin.ui.input.AlternativeAuthCallback

interface PinHelper {

    /**
     * Checks whether alternative authorization (probably biometric) is available on device
     */
    fun isAlternativeAuthAvailable(): Boolean

    /**
     * Check whether alternative authorization method is enabled by user
     */
    suspend fun isAlternativeAuthEnabled(): Boolean

    /**
     * Run alternative authorization flow and return authorization result into [callback]
     */
    fun openAlternativeAuth(host: Fragment, callback: AlternativeAuthCallback)

    /**
     * Navigate to screen with alternative authorization method setup
     */
    fun navigateToAlternativeAuthSetup(
        host: Fragment,
        resultDestinationId: Int,
        resultKey: String,
        pin: String,
    )
}
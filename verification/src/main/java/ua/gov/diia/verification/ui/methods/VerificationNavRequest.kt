package ua.gov.diia.verification.ui.methods

import androidx.navigation.NavDirections

fun interface VerificationNavRequest {

    /**
     * Returns navigation direction to the verification screen
     */
    fun getNavDirection(currentDestinationId: Int, resultKey: String): NavDirections
}
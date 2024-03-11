package ua.gov.diia.splash.helper

import androidx.fragment.app.Fragment

interface SplashHelper {

    /**
     * @return true if pin protection was set
     * */
    suspend fun isProtectionExists(): Boolean

    /**
     * saves pin for service user
     * */
    suspend fun setUserAuthorized(protectionKey: String)


    /**
     * Navigate the user to pin creation screen
     */
    fun navigateToProtectionCreation(
        host: Fragment,
        resultDestinationId: Int,
        resultKey: String,
    )
}
package ua.gov.diia.menu.helper

import androidx.fragment.app.Fragment

interface MenuHelper {

    /**
     * Navigate to all notifications
     */
    fun navigateToNotifications(fragment: Fragment)

    /**
     * Navigate to support
     */
    fun navigateToSupport(fragment: Fragment)

    /**
     * Navigate to help (outdated feature)
     */
    fun navigateToHelp(fragment: Fragment)

    /**
     * Navigate to settings
     */
    fun navigateToSettings(fragment: Fragment)

    /**
     * Navigate to FAQ
     */
    fun navigateToFAQ(fragment: Fragment)

    /**
     * Navigate to Logout dialog
     */
    fun navigateToLogout(fragment: Fragment)

    /**
     * Navigate to Diia ID flow
     */
    fun navigateToDiiaId(fragment: Fragment)

    /**
     * Navigate to Sign History flow
     */
    fun navigateToSignHistory(fragment: Fragment)

    /**
     * Navigate to AppSessions flow
     */
    fun navigateToAppSessions(fragment: Fragment)

    /**
     * Navigate to settings System dialog
     */
    fun navigateToSettingsSystemDialog(fragment: Fragment)

    /**
     * Navigate to Web view screen
     */
    fun navigateToWebViewUrl(fragment: Fragment, url: String)

}
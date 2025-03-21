package ua.gov.diia.feed.helper

import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import ua.gov.diia.core.models.notification.pull.PullNotificationItemSelection

interface FeedHelper {

    /**
     * Navigate to PublicService
     */
    fun navigateToPublicService(fragment: Fragment, serviceCode: String)

    /**
     * Navigate to all notifications
     */
    fun navigateToNotifications(fragment: Fragment)

    /**
     * Navigate to concrete notification
     */
    fun navigateToNotification(fragment: Fragment, notification: PullNotificationItemSelection)

    /**
     * Navigate by specific navDirection
     */
    fun navigateByNavDirection(fragment: Fragment, navDirection: NavDirections)
}
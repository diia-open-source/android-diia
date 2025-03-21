package ua.gov.diia.core.controller

import androidx.navigation.NavDirections
import ua.gov.diia.core.models.notification.pull.PullNotificationItemSelection

interface NotificationController {

    /**
     * Mark notification as read
     */
    suspend fun markAsRead(resId: String?)

    /**
     * Reload notification data
     */
    fun invalidateNotificationDataSource()

    /**
     * Load initial notifications data from network
     */
    suspend fun getNotificationsInitial()

    /**
     * Check if push token is synced and restart loading if it is not synced
     */
    fun checkPushTokenInSync()

    /**
     * Collect amount of unreaded notifications and return value in callback
     */
    suspend fun collectUnreadNotificationCounts(callback: (amount: Int) -> Unit)

    /**
     * Turn on notification
     */
    suspend fun allowNotifications()

    /**
     * Turn off notification
     */
    fun denyNotifications()

    /**
     * @return true if notification is allowed
     */
    suspend fun checkNotificationsRequested(): Boolean?

    suspend fun getNavDirectionForNotification(pullNotificationItemSelection: PullNotificationItemSelection): NavDirections?

    fun isMessageNotification(resourceType: String): Boolean
}
package ua.gov.diia.notifications.helper

import android.content.Intent
import androidx.navigation.NavDirections
import ua.gov.diia.core.models.notification.pull.PullNotificationItemSelection
import ua.gov.diia.core.models.notification.push.PushNotification

interface NotificationHelper {

    /**
     * Validate notification type and return true if type is message notification type
     * @return true if type is message notification type
     * */
    fun isMessageNotification(resourceType: String): Boolean

    /**
     * Provide navigation direction for further navigation
     * @return NavDirections of document or null if not require further navigation
     * */
    suspend fun navigateToDocument(item: PullNotificationItemSelection): NavDirections?

    /**
     * @return String that represent last update date of document in ISO8601
     * */
    suspend fun getLastDocumentUpdate(): String?

    /**
     * @return String that represent last active date in ISO8601
     * */
    suspend fun getLastActiveDate(): String?

    /**
     * @return Intent to the main activity of the current application
     * */
    fun getMainActivityIntent(): Intent

    /**
     * @return String that represent notification channel for this notification type
     * */
    fun getNotificationChannel(notif: PushNotification): String

    /**
     * Logging notifications data for debug purpose
     * */
    fun log(data: String)

}
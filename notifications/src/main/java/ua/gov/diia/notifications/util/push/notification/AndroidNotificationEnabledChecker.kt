package ua.gov.diia.notifications.util.push.notification

import android.content.Context
import androidx.core.app.NotificationManagerCompat
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class AndroidNotificationEnabledChecker @Inject constructor(
    @ApplicationContext private val context: Context
) : NotificationEnabledChecker {

    override fun notificationEnabled(): Boolean {
        return NotificationManagerCompat.from(context).areNotificationsEnabled()
    }
}
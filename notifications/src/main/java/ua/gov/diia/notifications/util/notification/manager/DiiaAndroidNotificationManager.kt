package ua.gov.diia.notifications.util.notification.manager

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import me.leolin.shortcutbadger.ShortcutBadger
import ua.gov.diia.core.util.delegation.WithCrashlytics
import ua.gov.diia.notifications.models.notification.push.DiiaNotificationChannel
import javax.inject.Inject

class DiiaAndroidNotificationManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val withCrashlytics: WithCrashlytics
) : DiiaNotificationManager {

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            initializeNotificationChannels()
        }
    }

    private val notificationManager: NotificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initializeNotificationChannels() {
        GlobalScope.launch {
            try {
                DiiaNotificationChannel.values().forEach {
                    if (notificationManager.getNotificationChannel(it.id) == null) {
                        val channel = NotificationChannel(
                            it.id,
                            context.getString(it.label),
                            NotificationManager.IMPORTANCE_HIGH
                        )
                        notificationManager.createNotificationChannel(channel)
                    }
                }
            } catch (e: Exception) {
                withCrashlytics.sendNonFatalError(e)
            }
        }
    }

    override fun clearNotification(notificationId: String) {
        val intId = notificationId.hashCode()
        notificationManager.cancel(intId)
    }

    override fun setBadeNumber(number: Int) {
        try {
            ShortcutBadger.applyCount(context, number)
        } catch (e: Exception) {
            withCrashlytics.sendNonFatalError(e)
        }
    }

    /**
     * In future we should avoid it and request documentAcquires from server side
     *
     * To make this solution completely work also implement notification channels on server side
     */
    @RequiresApi(Build.VERSION_CODES.O)
    override fun findDocumentAcquireInNotifications() {
        notificationManager.activeNotifications.forEach {
            if (it.packageName == context.packageName) {
                if (it.notification.channelId == DiiaNotificationChannel.ACQUIRER.id) {
                    it.notification.contentIntent.send()
                    notificationManager.cancel(it.tag, it.id)
                }
            }
        }
    }

}
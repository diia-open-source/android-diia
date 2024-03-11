package ua.gov.diia.notifications.util.notification.manager

import android.os.Build
import androidx.annotation.RequiresApi

interface DiiaNotificationManager {

    fun clearNotification(notificationId: String)

    fun setBadeNumber(number: Int)

    @RequiresApi(Build.VERSION_CODES.M)
    fun findDocumentAcquireInNotifications()
}
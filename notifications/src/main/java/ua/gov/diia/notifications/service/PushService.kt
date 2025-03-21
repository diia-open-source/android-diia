package ua.gov.diia.notifications.service

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import androidx.core.app.NotificationCompat
import androidx.lifecycle.MutableLiveData
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.work.WorkManager
import ua.gov.diia.analytics.DiiaAnalytics
import ua.gov.diia.core.CoreConstants
import ua.gov.diia.core.ExcludeFromJacocoGeneratedReport
import ua.gov.diia.core.di.actions.GlobalActionNotificationReceived
import ua.gov.diia.core.models.notification.push.PushNotification
import ua.gov.diia.core.util.CommonConst.BUILD_TYPE_RELEASE
import ua.gov.diia.core.util.deeplink.DeepLinkActionFactory
import ua.gov.diia.core.util.event.UiEvent
import ua.gov.diia.core.util.extensions.context.isDiiaAppRunning
import ua.gov.diia.core.util.extensions.getPendingFlags
import ua.gov.diia.diia_storage.DiiaStorage
import ua.gov.diia.notifications.BuildConfig
import ua.gov.diia.notifications.NotificationsConst.PUSH_NOTIFICATION_RECEIVED
import ua.gov.diia.notifications.R
import ua.gov.diia.notifications.action.ActionConstants.NOTIFICATION_TYPE_DOCUMENTS_SHARING
import ua.gov.diia.notifications.action.ActionConstants.NOTIFICATION_TYPE_PUSH_ACCESSIBILITY
import ua.gov.diia.notifications.action.ActionConstants.NOTIFICATION_TYPE_PUSH_BACKGROUND
import ua.gov.diia.notifications.helper.NotificationHelper
import ua.gov.diia.notifications.models.notification.push.getNotificationKey
import ua.gov.diia.notifications.store.NotificationsPreferences
import ua.gov.diia.notifications.util.notification.manager.DiiaNotificationManager
import ua.gov.diia.notifications.util.push.MoshiPushParser
import ua.gov.diia.notifications.work.DocWork
import ua.gov.diia.notifications.work.SendPushTokenWork
import ua.gov.diia.notifications.work.SilentPushWork

class PushService(
    private val context: Context,
    private val notificationHelper: NotificationHelper,
    private val deepLinkActionFactory: DeepLinkActionFactory,
    private val analytics: DiiaAnalytics,
    @GlobalActionNotificationReceived private val globalActionNotificationReceived: MutableLiveData<UiEvent>,
    val diiaStorage: DiiaStorage,
    val workManager: WorkManager,
    val notificationManager: DiiaNotificationManager,
) {
    fun onNewToken(token: String) {
        analytics.setPushToken(token)
        diiaStorage.apply {
            set(NotificationsPreferences.IsPushTokenSynced, false)
            set(NotificationsPreferences.PushToken, token)
        }

        SendPushTokenWork.enqueue(workManager, token)
    }

    fun processNotification(notificationJson: String) {
        if (BuildConfig.BUILD_TYPE != BUILD_TYPE_RELEASE) {
            notificationHelper.log(notificationJson)
        }
        onNotificationReceived()
        analytics.notificationReceived(notificationJson)
        globalActionNotificationReceived.postValue(UiEvent())
        MoshiPushParser().parsePushNotification(notificationJson)?.let {

            analytics.pushReceived(it.action.resourceId ?: "")

            when (it.action.type) {
                NOTIFICATION_TYPE_DOCUMENTS_SHARING -> {
                    if (context.isDiiaAppRunning()) {
                        context.startActivity(getIntentForNotification(it))
                    } else {
                        if (notificationsDisplayAllowed()) {
                            displayNotification(it)
                        } else {
                        }
                    }
                }

                NOTIFICATION_TYPE_PUSH_ACCESSIBILITY -> {
                    SilentPushWork.enqueue(workManager)
                }

                NOTIFICATION_TYPE_PUSH_BACKGROUND -> {
                    when (it.action.subtype) {
                        NOTIFICATION_SUB_TYPE_INTEGRITY -> {
                            LocalBroadcastManager
                                .getInstance(context)
                                .sendBroadcast(Intent(CoreConstants.CHECK_SAFETY_NET))
                        }

                        NOTIFICATION_SUB_TYPE_UPDATE_TO_ENGAGED -> {
                            DocWork.enqueue(workManager, NOTIFICATION_SUB_TYPE_UPDATE_TO_ENGAGED, it.action.resourceId ?: "")
                        }

                        NOTIFICATION_SYB_TYPE_DELETE_DOCUMENT -> {
                            DocWork.enqueue(workManager, NOTIFICATION_SYB_TYPE_DELETE_DOCUMENT, it.action.resourceId ?: "")
                        }

                        else -> {}
                    }
                }

                else -> {
                    if (notificationsDisplayAllowed()) {
                        displayNotification(it)
                    } else {

                    }
                }
            }
        }
    }

    private fun notificationsDisplayAllowed(): Boolean {
        return try {
            diiaStorage.getBoolean(
                NotificationsPreferences.AllowNotifications,
                true
            )
        } catch (e: Exception) {
            true
        }
    }

    @ExcludeFromJacocoGeneratedReport
    private fun displayNotification(pushNotification: PushNotification) {

        val channelId = notificationHelper.getNotificationChannel(pushNotification)

        val notificationKey = pushNotification.getNotificationKey()

        val notificationIntent = getIntentForNotification(pushNotification)

        val defaultSoundUri: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_push)
            .setContentTitle(pushNotification.title)
            .setContentText(pushNotification.shortText)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setChannelId(channelId)
            .setContentIntent(
                PendingIntent.getActivity(
                    context,
                    0,
                    notificationIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT or getPendingFlags()
                )
            )

        pushNotification.unread?.let {
            notificationManager.setBadeNumber(it)
        }

        notificationKey.let {
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.notify(
                it,
                notificationBuilder.build()
            )
        }
        analytics.pushShown(pushNotification.action.resourceId ?: "")
    }

    private fun onNotificationReceived() {
        LocalBroadcastManager
            .getInstance(context)
            .sendBroadcast(Intent(PUSH_NOTIFICATION_RECEIVED))
    }

    private fun getIntentForNotification(pushNotification: PushNotification): Intent {
        return notificationHelper.getMainActivityIntent().apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
            data = Uri.parse(deepLinkActionFactory.buildPathFromPushNotification(pushNotification))
        }
    }

    private companion object {
        const val NOTIFICATION_SUB_TYPE_INTEGRITY = "integrityCheck"
        const val NOTIFICATION_SUB_TYPE_UPDATE_TO_ENGAGED = "updateToEngaged"
        const val NOTIFICATION_SYB_TYPE_DELETE_DOCUMENT = "deleteDocument"
    }

}
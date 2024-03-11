package ua.gov.diia.notifications.service

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class FCMS : FirebaseMessagingService() {

    @Inject
    lateinit var pushService: PushService

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        pushService.onNewToken(token)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        remoteMessage.data[PAYLOAD]?.let {
            pushService.processNotification(it)
        }
    }

    companion object {
        private const val PAYLOAD = "payload"
    }

}
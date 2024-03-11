package ua.gov.diia.notifications.service

import com.huawei.hms.push.HmsMessageService
import com.huawei.hms.push.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HCMS : HmsMessageService() {

    @Inject
    lateinit var pushService: PushService

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        pushService.onNewToken(token)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
        super.onMessageReceived(remoteMessage)

        remoteMessage?.data?.let {
            pushService.processNotification(it)
        }
    }
}
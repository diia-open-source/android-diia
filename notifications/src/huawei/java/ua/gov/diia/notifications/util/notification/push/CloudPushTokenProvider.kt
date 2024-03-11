package ua.gov.diia.notifications.util.notification.push

import android.content.Context
import com.huawei.agconnect.config.AGConnectServicesConfig
import com.huawei.hms.aaid.HmsInstanceId
import dagger.hilt.android.qualifiers.ApplicationContext
import ua.gov.diia.notifications.util.notification.push.PushTokenProvider
import javax.inject.Inject


class CloudPushTokenProvider @Inject constructor(
    @ApplicationContext private val context: Context
) : PushTokenProvider {

    override fun requestCurrentPushToken(forceRefresh: Boolean): String {
        val appId = AGConnectServicesConfig.fromContext(context).getString("client/app_id")
        return HmsInstanceId.getInstance(context).getToken(appId, "HCM")
    }
}
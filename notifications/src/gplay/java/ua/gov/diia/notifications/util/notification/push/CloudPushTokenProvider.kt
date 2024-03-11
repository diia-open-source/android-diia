package ua.gov.diia.notifications.util.notification.push

import androidx.annotation.WorkerThread
import com.google.android.gms.tasks.Tasks
import com.google.firebase.messaging.FirebaseMessaging
import javax.inject.Inject

class CloudPushTokenProvider @Inject constructor() : PushTokenProvider {

    @WorkerThread
    @Throws(java.util.concurrent.ExecutionException::class)
    override fun requestCurrentPushToken(forceRefresh: Boolean): String {
        return Tasks.await(FirebaseMessaging.getInstance().token)
    }
}
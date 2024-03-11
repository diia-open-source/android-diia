package ua.gov.diia.notifications.work

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import ua.gov.diia.notifications.NotificationsConst.WORK_NAME_PUSH_TOKEN_UPDATE
import java.util.concurrent.TimeUnit

@HiltWorker
class SendPushTokenWork @AssistedInject constructor(
    @Assisted private val appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val sendPushTokenProcessor: SendPushTokenProcessor
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            val pushToken = inputData.getString(WORK_NAME_PUSH_TOKEN_UPDATE) ?: return Result.failure()
            sendPushTokenProcessor.syncPushNotification(pushToken)
        } catch (e: Exception) {
            Result.retry()
        }
    }

    companion object {
        private const val WORK_DELAY = 3L

        fun enqueue(workManager: WorkManager, pushToken: String) {
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

            val data = Data.Builder()
                .putString(WORK_NAME_PUSH_TOKEN_UPDATE, pushToken)
                .build()

            val sendPushTokenWork = OneTimeWorkRequest.Builder(SendPushTokenWork::class.java)
                .setConstraints(constraints)
                .setInputData(data)
                .setBackoffCriteria(
                    BackoffPolicy.EXPONENTIAL,
                    WORK_DELAY,
                    TimeUnit.SECONDS
                ).build()
            workManager.enqueueUniqueWork(WORK_NAME_PUSH_TOKEN_UPDATE, ExistingWorkPolicy.REPLACE, sendPushTokenWork)
        }
    }

}
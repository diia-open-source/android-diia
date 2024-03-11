package ua.gov.diia.notifications.work

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import ua.gov.diia.core.data.data_source.network.api.notification.ApiNotificationsPublic
import ua.gov.diia.diia_storage.store.repository.authorization.AuthorizationRepository
import ua.gov.diia.core.di.data_source.http.UnauthorizedClient
import ua.gov.diia.notifications.helper.NotificationHelper

@HiltWorker
class SilentPushWork @AssistedInject constructor(
    private val authorizationRepository: AuthorizationRepository,
    private val notificationHelper: NotificationHelper,
    @UnauthorizedClient private val notificationsPublic: ApiNotificationsPublic,
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            coroutineScope {
                val lastDocUpdate = async { notificationHelper.getLastDocumentUpdate() }
                val activityDate = async { notificationHelper.getLastActiveDate() }

                val headers = async {
                    val headers = mutableMapOf<String, String>()
                    val token = authorizationRepository.getToken()
                    if (token != null) headers[AUTH] = "$AUTH_BEARER $token"
                    headers
                }

                notificationsPublic.sendAppStatus(
                    headers = headers.await(),
                    appStatus = ua.gov.diia.core.models.AppStatus(
                        lastActivityDate = activityDate.await(),
                        lastDocumentUpdate = lastDocUpdate.await()
                    )
                )
            }
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }

    companion object {
        private const val AUTH = "Authorization"
        private const val AUTH_BEARER = "Bearer"
        private const val SILENT_PUSH_WORK = "silent_push_work"

        fun enqueue(workManager: WorkManager) {
            val silentWorkConstraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

            val silentPushWork = OneTimeWorkRequest.Builder(SilentPushWork::class.java)
                .setConstraints(silentWorkConstraints)
                .build()

            workManager.enqueueUniqueWork(
                SILENT_PUSH_WORK,
                ExistingWorkPolicy.REPLACE,
                silentPushWork
            )
        }
    }

}
package ua.gov.diia.core.util.work

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import ua.gov.diia.core.data.data_source.network.api.notification.ApiNotificationsPublic
import ua.gov.diia.core.data.repository.SystemRepository
import ua.gov.diia.core.di.data_source.http.UnauthorizedClient
import ua.gov.diia.core.util.delegation.WithBuildConfig
import java.net.UnknownHostException
import java.util.concurrent.TimeUnit

@HiltWorker
class CheckAppVersionUpdatedWork @AssistedInject constructor(
    @UnauthorizedClient private val apiNotificationsPublic: ApiNotificationsPublic,
    private val systemRepository: SystemRepository,
    @Assisted private val appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val buildConfig: WithBuildConfig
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result = if (runAttemptCount > RETRY_COUNT) {
        Result.failure()
    } else {
        try {
            val appVersionCode = systemRepository.getAppVersionCode()
            val currentAppVersionCode = buildConfig.getVersionCode()
            if (appVersionCode == null || currentAppVersionCode > appVersionCode) {
                apiNotificationsPublic.sendAppVersion()
            }
            systemRepository.setAppVersionCode(currentAppVersionCode)
            Result.success()
        } catch (e: UnknownHostException) {
            Result.retry()
        } catch (e: Exception) {
            Result.failure()
        }
    }

    companion object : WorkScheduler {
        private const val RETRY_COUNT = 3
        private const val VERSION_CHECK_WORK_DELAY = 5L

        override fun enqueue(workManager: WorkManager) {
            val workConstraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()
            val checkAppVersionWork = OneTimeWorkRequest
                .Builder(CheckAppVersionUpdatedWork::class.java)
                .setConstraints(workConstraints)
                .setBackoffCriteria(
                    BackoffPolicy.EXPONENTIAL,
                    VERSION_CHECK_WORK_DELAY,
                    TimeUnit.SECONDS
                )
                .build()
            workManager.enqueue(checkAppVersionWork)
        }
    }

}
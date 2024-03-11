package ua.gov.diia.core.util.work

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import ua.gov.diia.core.data.data_source.network.api.ApiSettings
import ua.gov.diia.core.di.data_source.http.UnauthorizedClient
import ua.gov.diia.core.util.extensions.context.isDiiaAppRunning
import ua.gov.diia.core.util.settings_action.SettingsActionExecutor
import java.util.concurrent.TimeUnit

/**
 * Trigger other workers including updating of push notification token
 */
@HiltWorker
class DoApplicationSettingsProvisionWork @AssistedInject constructor(
    @UnauthorizedClient private val apiSettings: ApiSettings,
    private val settingsActionExecutor: Set<@JvmSuppressWildcards SettingsActionExecutor>,
    @Assisted private val appContext: Context,
    @Assisted workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result = try {
        if (!appContext.isDiiaAppRunning()) {
            Result.failure()
        } else {
            val settings = apiSettings.appSettingsInfo()
            processActions(settings.actions)
            Result.success()
        }
    } catch (e: Exception) {
        Result.retry()
    }

    private suspend fun processActions(actions: List<String>?) {
        if (actions == null) return
        settingsActionExecutor.forEach { exec ->
            if (actions.contains(exec.actionKey)) {
                exec.executeAction()
            }
        }
    }

    companion object : WorkScheduler {
        private const val VERSION_CHECK_WORK_DELAY = 5L

        override fun enqueue(workManager: WorkManager) {
            val workConstraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

            val checkVersionWork = OneTimeWorkRequest
                .Builder(DoApplicationSettingsProvisionWork::class.java)
                .setConstraints(workConstraints)
                .setBackoffCriteria(
                    BackoffPolicy.LINEAR,
                    VERSION_CHECK_WORK_DELAY,
                    TimeUnit.SECONDS
                ).build()

            workManager.enqueueUniqueWork(
                "checkVersionWork",
                ExistingWorkPolicy.REPLACE,
                checkVersionWork
            )
        }
    }

}

package ua.gov.diia.opensource.ui.work

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import retrofit2.HttpException
import ua.gov.diia.core.di.data_source.http.UnauthorizedClient
import ua.gov.diia.core.network.Http
import ua.gov.diia.core.network.apis.ApiAuth
import java.util.concurrent.TimeUnit

@HiltWorker
class LogoutWork @AssistedInject constructor(
    @UnauthorizedClient private val apiAuth: ApiAuth,
    @Assisted private val appContext: Context,
    @Assisted workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        val mobileUuid = inputData.getString(LOGOUT_WORK_DATA_LOGOUT_UUID) ?: return Result.failure()
        val logoutToken = inputData.getString(LOGOUT_WORK_DATA_LOGOUT_TOKEN) ?: return Result.failure()
        val isServiceUser = inputData.getBoolean(LOGOUT_WORK_DATA_USER_TYPE, false)
        return try {
            if (isServiceUser) {
                apiAuth.logoutServiceUser("Bearer $logoutToken", mobileUuid)
            } else {
                apiAuth.logout("Bearer $logoutToken", mobileUuid)
            }
            Result.success()
        } catch (e: HttpException) {
            if (e.code() == Http.HTTP_401) {
                Result.success()
            } else {
                Result.retry()
            }
        } catch (e: Exception) {
            Result.retry()
        }
    }

    companion object {
        private const val LOGOUT_WORK_DATA_LOGOUT_TOKEN = "logout_token"
        private const val LOGOUT_WORK_DATA_LOGOUT_UUID = "logout_uuid"
        private const val LOGOUT_WORK_DATA_USER_TYPE = "logout_user_type"

        private const val LOGOUT_WORK_DELAY = 3L

        fun enqueue(
            workManager: WorkManager,
            logoutToken: String,
            mobileUuid: String,
            isServiceUser: Boolean
        ) {
            val logoutWorkConstraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()
            val data = Data.Builder()
                .putString(LOGOUT_WORK_DATA_LOGOUT_TOKEN, logoutToken)
                .putString(LOGOUT_WORK_DATA_LOGOUT_UUID, mobileUuid)
                .putBoolean(LOGOUT_WORK_DATA_USER_TYPE, isServiceUser)
                .build()
            workManager.enqueue(
                OneTimeWorkRequest.Builder(LogoutWork::class.java)
                    .setConstraints(logoutWorkConstraints)
                    .setBackoffCriteria(
                        BackoffPolicy.EXPONENTIAL,
                        LOGOUT_WORK_DELAY, TimeUnit.SECONDS
                    ).setInputData(data).build()
            )
        }

    }
}
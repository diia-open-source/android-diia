package ua.gov.diia.notifications.util.settings_action

import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.await
import ua.gov.diia.core.util.delegation.WithCrashlytics
import ua.gov.diia.core.util.settings_action.SettingsActionExecutor
import ua.gov.diia.diia_storage.DiiaStorage
import ua.gov.diia.notifications.NotificationsConst.WORK_NAME_PUSH_TOKEN_UPDATE
import ua.gov.diia.notifications.store.NotificationsPreferences.PushToken
import ua.gov.diia.notifications.util.notification.push.PushTokenProvider
import ua.gov.diia.notifications.work.SendPushTokenWork
import javax.inject.Inject

class PushTokenUpdateActionExecutor @Inject constructor(
    private val pushTokenProvider: PushTokenProvider,
    private val workManager: WorkManager,
    private val diiaStorage: DiiaStorage,
    private val withCrashlytics: WithCrashlytics
) : SettingsActionExecutor {

    override val actionKey: String
        get() = "pushTokenUpdate"

    override suspend fun executeAction() {
        val work = workManager.getWorkInfosForUniqueWork(WORK_NAME_PUSH_TOKEN_UPDATE).await()
        if (!work.isRunning()) {
            var pushToken = diiaStorage.get(PushToken, null) as? String
            if (pushToken == null) {
                //In case push token is not saved, we should trigger new token generation process
                //PushService onNewToken() will be triggered
                try {
                    pushToken = pushTokenProvider.requestCurrentPushToken(forceRefresh = false)
                } catch (e: java.util.concurrent.ExecutionException) {
                    withCrashlytics.sendNonFatalError(e)
                    return
                }
            }
            SendPushTokenWork.enqueue(workManager, pushToken)
        }
    }

    private fun List<WorkInfo>.isRunning(): Boolean {
        return if (isNotEmpty()) {
            this[0].state == WorkInfo.State.RUNNING || this[0].state == WorkInfo.State.ENQUEUED
        } else {
            false
        }
    }

}
package ua.gov.diia.notifications.work

import androidx.work.ListenableWorker
import ua.gov.diia.core.data.data_source.network.api.notification.ApiNotificationsPublic
import ua.gov.diia.core.di.data_source.http.AuthorizedClient
import ua.gov.diia.core.models.PushToken
import ua.gov.diia.diia_storage.store.repository.authorization.AuthorizationRepository
import ua.gov.diia.notifications.store.datasource.notifications.KeyValueNotificationDataSource
import javax.inject.Inject

class SendPushTokenProcessor @Inject constructor(
    @AuthorizedClient private val apiNotificationsPublic: ApiNotificationsPublic,
    private val authorizationRepository: AuthorizationRepository,
    private val keyValueSource: KeyValueNotificationDataSource,
) {

    suspend fun syncPushNotification(pushToken: String): ListenableWorker.Result {
        keyValueSource.setPushToken(pushToken)
        val authToken = authorizationRepository.getToken()
        if (authToken == null) {
            return ListenableWorker.Result.success()
        } else {
            apiNotificationsPublic.sendDeviceUserPushToken(
                PushToken(pushToken)
            )
            keyValueSource.setIsPushTokenSynced(synced = true)
        }
        return ListenableWorker.Result.success()
    }
}
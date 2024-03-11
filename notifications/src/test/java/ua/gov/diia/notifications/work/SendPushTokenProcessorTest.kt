package ua.gov.diia.notifications.work

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.work.ListenableWorker
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import ua.gov.diia.core.data.data_source.network.api.notification.ApiNotificationsPublic
import ua.gov.diia.core.models.PushToken
import ua.gov.diia.diia_storage.store.repository.authorization.AuthorizationRepository
import ua.gov.diia.notifications.MainDispatcherRule
import ua.gov.diia.notifications.store.datasource.notifications.KeyValueNotificationDataSource

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class SendPushTokenProcessorTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()
    lateinit var apiNotificationsPublic: ApiNotificationsPublic
    lateinit var authorizationRepository: AuthorizationRepository
    lateinit var keyValueSource: KeyValueNotificationDataSource

    lateinit var sendPushTokenProcessor: SendPushTokenProcessor

    @Before
    fun setUp() {
        apiNotificationsPublic = mockk(relaxed = true)
        authorizationRepository = mockk(relaxed = true)
        keyValueSource = mockk(relaxed = true)

        sendPushTokenProcessor = SendPushTokenProcessor(apiNotificationsPublic, authorizationRepository, keyValueSource)
    }

    @Test
    fun `test syncPushNotification send push token and authToken is null`() {
        runBlocking {
            val token = "token"
            coEvery { authorizationRepository.getToken() } returns null
            val result = sendPushTokenProcessor.syncPushNotification(token)

            coVerify (exactly = 1) { keyValueSource.setPushToken(token) }
            coVerify (exactly = 1) { authorizationRepository.getToken() }
            coVerify (exactly = 0) { apiNotificationsPublic.sendDeviceUserPushToken(PushToken(token)) }
            coVerify (exactly = 0) { keyValueSource.setIsPushTokenSynced(synced = true) }
            assertEquals(ListenableWorker.Result.success(), result)
        }
    }

    @Test
    fun `test syncPushNotification send push token and authToken is not null`() {
        runBlocking {
            val token = "token"
            val authToken = "authToken"
            coEvery { authorizationRepository.getToken() } returns authToken

            val result = sendPushTokenProcessor.syncPushNotification(token)

            coVerify (exactly = 1) { keyValueSource.setPushToken(token) }
            coVerify (exactly = 1) { authorizationRepository.getToken() }
            coVerify (exactly = 1) { apiNotificationsPublic.sendDeviceUserPushToken(PushToken(token)) }
            coVerify (exactly = 1) { keyValueSource.setIsPushTokenSynced(synced = true) }
            assertEquals(ListenableWorker.Result.success(), result)
        }
    }
}
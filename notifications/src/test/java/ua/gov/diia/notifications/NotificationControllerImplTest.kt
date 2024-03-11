package ua.gov.diia.notifications

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.work.WorkManager
import io.mockk.clearMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import ua.gov.diia.diia_storage.store.Preferences
import ua.gov.diia.notifications.store.datasource.notifications.KeyValueNotificationDataSource
import ua.gov.diia.notifications.store.datasource.notifications.NotificationDataRepository
import ua.gov.diia.notifications.util.notification.manager.DiiaNotificationManager
import ua.gov.diia.notifications.util.notification.push.PushTokenProvider
import ua.gov.diia.notifications.work.SendPushTokenWork

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class NotificationControllerImplTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    lateinit var workManager: WorkManager
    lateinit var notificationsDataSource: NotificationDataRepository
    lateinit var notificationManager: DiiaNotificationManager
    lateinit var pushTokenProvider: PushTokenProvider
    lateinit var keyValueSource: KeyValueNotificationDataSource

    lateinit var notificationControllerImpl: NotificationControllerImpl

    private val unreadCount = MutableStateFlow(0)

    @Before
    fun setUp() {

        workManager = mockk(relaxed = true)
        notificationsDataSource = mockk(relaxed = true)
        notificationManager = mockk(relaxed = true)
        pushTokenProvider = mockk(relaxed = true)
        keyValueSource = mockk(relaxed = true)

        coEvery { notificationsDataSource.unreadCount } returns unreadCount
        notificationControllerImpl = NotificationControllerImpl(
            workManager,
            notificationsDataSource,
            notificationManager,
            pushTokenProvider,
            keyValueSource
        )
    }

    @Test
    fun `test markAsRead`() {
        runBlocking {
            val resId = "resId"
            notificationControllerImpl.markAsRead(resId)

            coVerify(exactly = 1) { notificationsDataSource.markNotificationAsRead(resId) }

            //Not mark data if is not def
            clearMocks(notificationsDataSource)
            notificationControllerImpl.markAsRead(Preferences.DEF)

            coVerify(exactly = 0) { notificationsDataSource.markNotificationAsRead(resId) }

            //Not mark data if redId is null
            clearMocks(notificationsDataSource)
            notificationControllerImpl.markAsRead(null)

            coVerify(exactly = 0) { notificationsDataSource.markNotificationAsRead(resId) }
        }
    }

    @Test
    fun `test invalidateNotificationDataSource calls invalidate in data source`() {
        runBlocking {
            notificationControllerImpl.invalidateNotificationDataSource()

            coVerify(exactly = 1) { notificationsDataSource.invalidate() }
        }
    }

    @Test
    fun `test getNotificationsInitial calls loadDataFromNetwork in data source`() {
        runBlocking {
            notificationControllerImpl.getNotificationsInitial()

            coVerify(exactly = 1) { notificationsDataSource.loadDataFromNetwork(0, 5, any()) }
        }
    }

    @Test
    fun `test checkPushTokenInSync return that token is not synced`() {
        runBlocking {
            val token = "token"
            mockkObject(SendPushTokenWork)
            justRun { SendPushTokenWork.enqueue(workManager, token) }

            every { keyValueSource.isPushTokenSynced() } returns false
            every { pushTokenProvider.requestCurrentPushToken() } returns token

            notificationControllerImpl.checkPushTokenInSync()

            coVerify(exactly = 1) { pushTokenProvider.requestCurrentPushToken() }
            coVerify(exactly = 1) { keyValueSource.isPushTokenSynced() }
            verify { SendPushTokenWork.enqueue(workManager, token) }
        }
    }

    @Test
    fun `test checkPushTokenInSync return that token is not synced but token is empty`() {
        runBlocking {
            val token = ""
            mockkObject(SendPushTokenWork)
            every { keyValueSource.isPushTokenSynced() } returns false
            every { pushTokenProvider.requestCurrentPushToken() } returns token

            notificationControllerImpl.checkPushTokenInSync()

            coVerify(exactly = 1) { pushTokenProvider.requestCurrentPushToken() }
            coVerify(exactly = 1) { keyValueSource.isPushTokenSynced() }
            verify(exactly = 0) { SendPushTokenWork.enqueue(workManager, token) }
        }
    }

    @Test
    fun `test checkPushTokenInSync not call sync if toke is synced`() {
        runBlocking {
            mockkObject(SendPushTokenWork)
            every { keyValueSource.isPushTokenSynced() } returns true

            notificationControllerImpl.checkPushTokenInSync()

            coVerify(exactly = 1) { keyValueSource.isPushTokenSynced() }
            coVerify(exactly = 0) { pushTokenProvider.requestCurrentPushToken() }
            verify(exactly = 0) { SendPushTokenWork.enqueue(workManager, any()) }
        }
    }

    @Test
    fun `test allowNotifications`() {
        runBlocking {
            notificationControllerImpl.allowNotifications()

            coVerify(exactly = 1) { keyValueSource.allowNotifications() }
        }
    }

    @Test
    fun `test denyNotifications`() {
        runBlocking {
            notificationControllerImpl.denyNotifications()

            coVerify(exactly = 1) { keyValueSource.denyNotifications() }
        }
    }

    @Test
    fun `test collectUnreadNotificationCounts`() {
        runTest {
            val sendValue = 10
            var callbackValue = -1
            unreadCount.emit(sendValue)
            val job = launch {
                notificationControllerImpl.collectUnreadNotificationCounts {
                    callbackValue = it
                }
            }
            advanceUntilIdle()

            coVerify(exactly = 1) { notificationManager.setBadeNumber(10) }
            assertEquals(sendValue, callbackValue)
            job.cancel()
        }
    }
}
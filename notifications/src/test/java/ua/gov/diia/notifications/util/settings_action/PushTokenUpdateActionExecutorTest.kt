package ua.gov.diia.notifications.util.settings_action

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.google.common.util.concurrent.ListenableFuture
import io.mockk.coVerify
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import ua.gov.diia.core.util.delegation.WithCrashlytics
import ua.gov.diia.diia_storage.DiiaStorage
import ua.gov.diia.notifications.MainDispatcherRule
import ua.gov.diia.notifications.NotificationsConst
import ua.gov.diia.notifications.store.NotificationsPreferences
import ua.gov.diia.notifications.util.notification.push.PushTokenProvider
import ua.gov.diia.notifications.work.SendPushTokenWork
import java.util.concurrent.ExecutionException
import java.util.concurrent.Executor
import java.util.concurrent.TimeUnit

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class PushTokenUpdateActionExecutorTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    lateinit var pushTokenProvider: PushTokenProvider
    lateinit var workManager: WorkManager
    lateinit var diiaStorage: DiiaStorage
    lateinit var withCrashlytics: WithCrashlytics

    lateinit var pushTokenUpdateActionExecutor: PushTokenUpdateActionExecutor

    @Before
    fun setUp() {
        pushTokenProvider = mockk()
        workManager = mockk()
        diiaStorage = mockk()
        withCrashlytics = mockk(relaxed = true)

        pushTokenUpdateActionExecutor = PushTokenUpdateActionExecutor(pushTokenProvider, workManager, diiaStorage, withCrashlytics)
    }

    fun prepListenableFuture(state: WorkInfo.State = WorkInfo.State.SUCCEEDED) {
        val workInfo = mockk<WorkInfo>(relaxed = true)
        every { workInfo.state } returns state

        val feature = object : ListenableFuture<List<WorkInfo>> {
            override fun cancel(mayInterruptIfRunning: Boolean): Boolean {
                return false
            }
            override fun isCancelled(): Boolean {
                return false
            }
            override fun isDone(): Boolean {
                return true
            }
            override fun get(): List<WorkInfo> {
                return listOf(workInfo)
            }
            override fun get(timeout: Long, unit: TimeUnit?): List<WorkInfo> {
                return listOf(workInfo)
            }
            override fun addListener(listener: Runnable, executor: Executor) {}
        }

        every { workManager.getWorkInfosForUniqueWork(any()) } returns feature
    }
    @Test
    fun `test executeAction`() {
        runBlocking {
            val token = "token"
            mockkObject(SendPushTokenWork)
            justRun { SendPushTokenWork.enqueue(workManager, token) }
            prepListenableFuture()

            every { diiaStorage.get(NotificationsPreferences.PushToken, null) } returns null
            every { pushTokenProvider.requestCurrentPushToken(forceRefresh = false) } returns token

            pushTokenUpdateActionExecutor.executeAction()

            verify(exactly = 1) { workManager.getWorkInfosForUniqueWork(NotificationsConst.WORK_NAME_PUSH_TOKEN_UPDATE) }
            verify(exactly = 1) { diiaStorage.get(NotificationsPreferences.PushToken, null) }
            verify(exactly = 1) { pushTokenProvider.requestCurrentPushToken(forceRefresh = false) }
            verify { SendPushTokenWork.enqueue(workManager, token) }
        }
    }
    @Test
    fun `test executeAction not requestCurrentPushToken if token is not null`() {
        runBlocking {
            val token = "token"
            mockkObject(SendPushTokenWork)
            justRun { SendPushTokenWork.enqueue(workManager, token) }
            prepListenableFuture()

            every { diiaStorage.get(NotificationsPreferences.PushToken, null) } returns token

            pushTokenUpdateActionExecutor.executeAction()

            verify(exactly = 0) { pushTokenProvider.requestCurrentPushToken(forceRefresh = false) }
        }
    }

    @Test
    fun `test executeAction send non fatal if requestCurrentPushToken throw error`() {
        runBlocking {
            val token = "token"
            mockkObject(SendPushTokenWork)
            justRun { SendPushTokenWork.enqueue(workManager, token) }
            prepListenableFuture()
            every { diiaStorage.get(NotificationsPreferences.PushToken, null) } returns null
            val exception = mockk<ExecutionException>()
            every { pushTokenProvider.requestCurrentPushToken(forceRefresh = false) } throws exception

            pushTokenUpdateActionExecutor.executeAction()

            coVerify(exactly = 1) { withCrashlytics.sendNonFatalError(exception) }
        }
    }

    @Test
    fun `test actionKey validation`() {
        assertEquals("pushTokenUpdate", pushTokenUpdateActionExecutor.actionKey)
    }

    @Test
    fun `test executeAction if feature list is empty`() {
        runBlocking {
            val token = "token"
            mockkObject(SendPushTokenWork)
            justRun { SendPushTokenWork.enqueue(workManager, token) }

            val feature = object : ListenableFuture<List<WorkInfo>> {
                override fun cancel(mayInterruptIfRunning: Boolean): Boolean {
                    return false
                }
                override fun isCancelled(): Boolean {
                    return false
                }
                override fun isDone(): Boolean {
                    return true
                }
                override fun get(): List<WorkInfo> {
                    return listOf()
                }
                override fun get(timeout: Long, unit: TimeUnit?): List<WorkInfo> {
                    return listOf()
                }
                override fun addListener(listener: Runnable, executor: Executor) {}
            }

            every { workManager.getWorkInfosForUniqueWork(any()) } returns feature

            every { diiaStorage.get(NotificationsPreferences.PushToken, null) } returns null
            every { pushTokenProvider.requestCurrentPushToken(forceRefresh = false) } returns token

            pushTokenUpdateActionExecutor.executeAction()

            verify(exactly = 1) { workManager.getWorkInfosForUniqueWork(NotificationsConst.WORK_NAME_PUSH_TOKEN_UPDATE) }
            verify(exactly = 1) { diiaStorage.get(NotificationsPreferences.PushToken, null) }
            verify(exactly = 1) { pushTokenProvider.requestCurrentPushToken(forceRefresh = false) }
            verify { SendPushTokenWork.enqueue(workManager, token) }
        }
    }
    @Test
    fun `test executeAction only with worker in RUNNING state`() {
        runBlocking {
            val token = "token"
            mockkObject(SendPushTokenWork)
            justRun { SendPushTokenWork.enqueue(workManager, token) }
            val workInfo = mockk<WorkInfo>(relaxed = true)
            every { workInfo.state } returns WorkInfo.State.RUNNING

            val feature = object : ListenableFuture<List<WorkInfo>> {
                override fun cancel(mayInterruptIfRunning: Boolean): Boolean {
                    return false
                }
                override fun isCancelled(): Boolean {
                    return false
                }
                override fun isDone(): Boolean {
                    return true
                }
                override fun get(): List<WorkInfo> {
                    return listOf(workInfo)
                }
                override fun get(timeout: Long, unit: TimeUnit?): List<WorkInfo> {
                    return listOf(workInfo)
                }
                override fun addListener(listener: Runnable, executor: Executor) {}
            }

            every { workManager.getWorkInfosForUniqueWork(any()) } returns feature

            every { diiaStorage.get(NotificationsPreferences.PushToken, null) } returns null
            every { pushTokenProvider.requestCurrentPushToken(forceRefresh = false) } returns token

            pushTokenUpdateActionExecutor.executeAction()

            verify(exactly = 1) { workManager.getWorkInfosForUniqueWork(NotificationsConst.WORK_NAME_PUSH_TOKEN_UPDATE) }
            verify(exactly = 0) { diiaStorage.get(NotificationsPreferences.PushToken, null) }
            verify(exactly = 0) { pushTokenProvider.requestCurrentPushToken(forceRefresh = false) }
        }
    }

    @Test
    fun `test executeAction only with worker in ENQUEUED state`() {
        runBlocking {
            val token = "token"
            mockkObject(SendPushTokenWork)
            justRun { SendPushTokenWork.enqueue(workManager, token) }
            val workInfo = mockk<WorkInfo>(relaxed = true)
            every { workInfo.state } returns WorkInfo.State.ENQUEUED

            val feature = object : ListenableFuture<List<WorkInfo>> {
                override fun cancel(mayInterruptIfRunning: Boolean): Boolean {
                    return false
                }
                override fun isCancelled(): Boolean {
                    return false
                }
                override fun isDone(): Boolean {
                    return true
                }
                override fun get(): List<WorkInfo> {
                    return listOf(workInfo)
                }
                override fun get(timeout: Long, unit: TimeUnit?): List<WorkInfo> {
                    return listOf(workInfo)
                }
                override fun addListener(listener: Runnable, executor: Executor) {}
            }

            every { workManager.getWorkInfosForUniqueWork(any()) } returns feature

            every { diiaStorage.get(NotificationsPreferences.PushToken, null) } returns null
            every { pushTokenProvider.requestCurrentPushToken(forceRefresh = false) } returns token

            pushTokenUpdateActionExecutor.executeAction()

            verify(exactly = 1) { workManager.getWorkInfosForUniqueWork(NotificationsConst.WORK_NAME_PUSH_TOKEN_UPDATE) }
            verify(exactly = 0) { diiaStorage.get(NotificationsPreferences.PushToken, null) }
            verify(exactly = 0) { pushTokenProvider.requestCurrentPushToken(forceRefresh = false) }
        }
    }

}
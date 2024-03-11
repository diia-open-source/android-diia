package ua.gov.diia.core.util.work

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerParameters
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import ua.gov.diia.core.data.data_source.network.api.notification.ApiNotificationsPublic
import ua.gov.diia.core.data.repository.SystemRepository
import ua.gov.diia.core.util.delegation.WithBuildConfig
import java.net.UnknownHostException

class CheckAppVersionUpdatedWorkTest {

    lateinit var context: Context
    lateinit var apiNotifications: ApiNotificationsPublic
    lateinit var systemRepository: SystemRepository
    lateinit var workerParams: WorkerParameters
    lateinit var buildConfig: WithBuildConfig

    lateinit var checkAppVersionUpdatedWork: CheckAppVersionUpdatedWork

    @Before
    fun before() {
        context = mockk(relaxed = true)
        apiNotifications = mockk(relaxed = true)
        systemRepository = mockk(relaxed = true)
        workerParams = mockk(relaxed = true)
        buildConfig = mockk(relaxed = true)

        checkAppVersionUpdatedWork = CheckAppVersionUpdatedWork(
            apiNotifications,
            systemRepository,
            context,
            workerParams,
            buildConfig
        )
    }

    @Test
    fun `should return failure if runAttemptCount more than 3`() = runTest {
        every { workerParams.runAttemptCount } returns 4
        Assert.assertEquals(
            ListenableWorker.Result.failure(),
            checkAppVersionUpdatedWork.doWork(),
        )
    }

    @Test
    fun `should return retry if there no internet connection`() = runTest {
        every { workerParams.runAttemptCount } returns 0
        coEvery { systemRepository.getAppVersionCode() } returns null
        every { buildConfig.getVersionCode() } returns 0
        coEvery { apiNotifications.sendAppVersion(any()) } throws UnknownHostException()
        Assert.assertEquals(
            ListenableWorker.Result.retry(),
            checkAppVersionUpdatedWork.doWork()
        )
    }

    @Test
    fun `should return success`() = runTest {
        every { workerParams.runAttemptCount } returns 0
       coJustRun { systemRepository.setAppVersionCode(any()) }
        coEvery { systemRepository.getAppVersionCode() } returns 1
        every { buildConfig.getVersionCode() } returns 2
        Assert.assertEquals(
            ListenableWorker.Result.success(),
            checkAppVersionUpdatedWork.doWork()
        )
    }
    @After
    fun after() {
        clearAllMocks()
    }
}
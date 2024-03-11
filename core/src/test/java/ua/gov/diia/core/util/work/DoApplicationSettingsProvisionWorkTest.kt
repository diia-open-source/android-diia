package ua.gov.diia.core.util.work

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerParameters
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import ua.gov.diia.core.data.data_source.network.api.ApiSettings
import ua.gov.diia.core.models.appversion.AppSettingsInfo
import ua.gov.diia.core.util.extensions.context.isDiiaAppRunning
import ua.gov.diia.core.util.settings_action.SettingsActionExecutor
import java.net.UnknownHostException

class DoApplicationSettingsProvisionWorkTest {

    lateinit var context: Context

    lateinit var apiSettings: ApiSettings
    lateinit var settingsActionExecutor: MutableSet<@JvmSuppressWildcards SettingsActionExecutor>

    lateinit var workerParams: WorkerParameters

    lateinit var doApplicationSettingsProvisionWork: DoApplicationSettingsProvisionWork

    @Before
    fun before() {
        context = mockk(relaxed = true)
        apiSettings = mockk(relaxed = true)
        workerParams = mockk(relaxed = true)
        settingsActionExecutor = mutableSetOf()

        doApplicationSettingsProvisionWork = DoApplicationSettingsProvisionWork(
            apiSettings,
            settingsActionExecutor,
            context,
            workerParams,
        )
    }

    @Test
    fun `should return failure if diia app is running`() = runTest {
        mockkStatic(Context::isDiiaAppRunning)
        every { context.isDiiaAppRunning() } returns false
        Assert.assertEquals(
            ListenableWorker.Result.failure(),
            doApplicationSettingsProvisionWork.doWork(),
        )
    }

    @Test
    fun `should return retry if there no internet connection`() = runTest {
        mockkStatic(Context::isDiiaAppRunning)
        every { context.isDiiaAppRunning() } returns true
        coEvery { apiSettings.appSettingsInfo() } throws UnknownHostException()
        Assert.assertEquals(
            ListenableWorker.Result.retry(),
            doApplicationSettingsProvisionWork.doWork()
        )
    }

    @Test
    fun `should return success and call appSettingsInfo`() = runTest {
        mockkStatic(Context::isDiiaAppRunning)
        every { context.isDiiaAppRunning() } returns true
        val appSettingsInfo = mockk<AppSettingsInfo>(relaxed = true)
        coEvery { apiSettings.appSettingsInfo() } returns appSettingsInfo
        every { appSettingsInfo.actions } returns null

        Assert.assertEquals(
            ListenableWorker.Result.success(),
            doApplicationSettingsProvisionWork.doWork()
        )
        coVerify { apiSettings.appSettingsInfo() }
    }


    @Test
    fun `should execute executor if its key in action list`() = runTest {
        val executorOneKey = "executor1"
        val executor1 = mockk<SettingsActionExecutor>(relaxed = true)
        every { executor1.actionKey } returns executorOneKey
        val executor2 = mockk<SettingsActionExecutor>(relaxed = true)
        every { executor2.actionKey } returns "executor2"
        settingsActionExecutor.add(executor1)
        settingsActionExecutor.add(executor2)

        mockkStatic(Context::isDiiaAppRunning)
        every { context.isDiiaAppRunning() } returns true
        val appSettingsInfo = mockk<AppSettingsInfo>(relaxed = true)
        coEvery { apiSettings.appSettingsInfo() } returns appSettingsInfo
        val actions = mutableListOf<String>()
        actions.add(executorOneKey)
        every { appSettingsInfo.actions } returns actions

        Assert.assertEquals(
            ListenableWorker.Result.success(),
            doApplicationSettingsProvisionWork.doWork()
        )
        coVerify(exactly = 1) { executor1.executeAction() }
        coVerify(exactly = 0) { executor2.executeAction() }
    }

    @After
    fun after() {
        clearAllMocks()
    }
}
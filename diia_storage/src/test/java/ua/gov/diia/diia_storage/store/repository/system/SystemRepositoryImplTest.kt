package ua.gov.diia.diia_storage.store.repository.system

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import ua.gov.diia.core.util.DispatcherProvider
import ua.gov.diia.diia_storage.CommonPreferenceKeys
import ua.gov.diia.diia_storage.DiiaStorage
import ua.gov.diia.diia_storage.MainDispatcherRule

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class SystemRepositoryImplTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    lateinit var diiaStorage: DiiaStorage
    lateinit var dispatcherProvider: DispatcherProvider
    lateinit var systemRepositoryImpl: SystemRepositoryImpl

    @Before
    fun before() {
        diiaStorage = mockk(relaxed = true)
        dispatcherProvider = mockk(relaxed = true)
        every { dispatcherProvider.work } returns UnconfinedTestDispatcher()

        systemRepositoryImpl = SystemRepositoryImpl(dispatcherProvider, diiaStorage)
    }

    @Test
    fun `getAppVersionCode returns null if code is -1`() = runTest {
        every { diiaStorage.getInt(CommonPreferenceKeys.CurrentAppVersion, -1) } returns -1
        val appVersionCode = systemRepositoryImpl.getAppVersionCode()

        coVerify(exactly = 1) { diiaStorage.getInt(CommonPreferenceKeys.CurrentAppVersion, -1) }
        assertNull(appVersionCode)
    }

    @Test
    fun `getAppVersionCode returns code if storage returns code`() = runTest {
        every { diiaStorage.getInt(CommonPreferenceKeys.CurrentAppVersion, -1) } returns 10
        val appVersionCode = systemRepositoryImpl.getAppVersionCode()

        coVerify(exactly = 1) { diiaStorage.getInt(CommonPreferenceKeys.CurrentAppVersion, -1) }
        assertEquals(10, appVersionCode)
    }


    @Test
    fun `setAppVersionCode saves code into storage`() = runTest {
        systemRepositoryImpl.setAppVersionCode(10)

        coVerify(exactly = 1) { diiaStorage.set(CommonPreferenceKeys.CurrentAppVersion, 10) }
    }
}
package ua.gov.diia.biometric.store

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockitokotlin2.verify
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
import ua.gov.diia.biometric.rules.MainDispatcherRule
import ua.gov.diia.core.util.DispatcherProvider
import ua.gov.diia.diia_storage.store.Preferences
import ua.gov.diia.diia_storage.DiiaStorage

@RunWith(MockitoJUnitRunner::class)
class BiometricRepositoryImplTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    lateinit var diiaStorage: DiiaStorage

    @Mock
    lateinit var dispatcherProvider: DispatcherProvider

    lateinit var biometricRepositoryImpl: BiometricRepositoryImpl

    @Before
    fun setUp() {
        `when`(dispatcherProvider.work).thenReturn(UnconfinedTestDispatcher())

        biometricRepositoryImpl = BiometricRepositoryImpl(
            diiaStorage = diiaStorage,
            dispatcherProvider = dispatcherProvider
        )
    }

    @Test
    fun `test isBiometricAuthEnabled get UseTouchId from storage `() = runTest {
        runBlocking {
            `when`(diiaStorage.getBoolean(Preferences.UseTouchId, false)).thenReturn(true)

            val result = biometricRepositoryImpl.isBiometricAuthEnabled()

            verify(diiaStorage).getBoolean(Preferences.UseTouchId, false)
            assertTrue(result)
        }
    }

    @Test
    fun `test enableBiometricAuth save UseTouchId value to storage `() = runTest {
        runBlocking {
            biometricRepositoryImpl.enableBiometricAuth(true)

            verify(diiaStorage).set(Preferences.UseTouchId, true)
        }
    }
}
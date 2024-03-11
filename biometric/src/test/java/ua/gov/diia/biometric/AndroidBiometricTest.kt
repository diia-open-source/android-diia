package ua.gov.diia.biometric

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.biometric.BiometricManager
import io.mockk.*
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import ua.gov.diia.biometric.Biometric.Companion.FACE_IN_USE
import ua.gov.diia.biometric.rules.MainDispatcherRule
import ua.gov.diia.core.util.delegation.WithBuildConfig

@RunWith(MockitoJUnitRunner::class)
class AndroidBiometricTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    lateinit var androidBiometric: AndroidBiometric

    lateinit var context: Context
    lateinit var withBuildConfig: WithBuildConfig

    @Before
    fun setUp() {
        context = mockk()
        withBuildConfig = mockk()
        androidBiometric = AndroidBiometric(context, withBuildConfig)
    }

    fun prepareIsBiometricAuthAvailable() {
        val biometricManager: BiometricManager = mockk()
        every { biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG) } returns BiometricManager.BIOMETRIC_SUCCESS
        mockkStatic(BiometricManager::class)
        every { BiometricManager.from(context) } returns biometricManager
    }
    @Test
    fun `test isBiometricAuthAvailable returns true if biometric is strong `() {
        runBlocking {
            prepareIsBiometricAuthAvailable()

            assertTrue(androidBiometric.isBiometricAuthAvailable())
        }
    }
    @Test
    fun `test isBiometricAuthAvailable returns false if biometric is not strong`() {
        runBlocking {
            val biometricManager: BiometricManager = mockk()
            every { biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG) } returns BiometricManager.BIOMETRIC_STATUS_UNKNOWN
            mockkStatic(BiometricManager::class)
            every { BiometricManager.from(context) } returns biometricManager
            assertFalse(androidBiometric.isBiometricAuthAvailable())
        }
    }

    @Test
    fun `test hasFingerprint`() {
        runBlocking {
            val packageManager = mockk<PackageManager>()
            every { context.packageManager } returns packageManager
            every { packageManager.hasSystemFeature(PackageManager.FEATURE_FINGERPRINT) } returns true

            assertTrue(androidBiometric.hasFingerprint())
            verify(exactly = 1) { context.packageManager }
            verify(exactly = 1) { packageManager.hasSystemFeature(PackageManager.FEATURE_FINGERPRINT) }
        }
    }

    @Test
    fun `test hasFace`() {
        runBlocking {
            val packageManager = mockk<PackageManager>()
            every { context.packageManager } returns packageManager
            every { packageManager.hasSystemFeature(PackageManager.FEATURE_FACE) } returns true
            every { withBuildConfig.getSdkVersion() } returns Build.VERSION_CODES.Q

            assertTrue(androidBiometric.hasFace())
            verify(exactly = 1) { context.packageManager }
            verify(exactly = 1) { packageManager.hasSystemFeature(PackageManager.FEATURE_FACE) }
        }
    }

    @Test
    fun `test hasFace return false if SDK is less than Q`() {
        runBlocking {
            val packageManager = mockk<PackageManager>()
            every { context.packageManager } returns packageManager
            every { packageManager.hasSystemFeature(PackageManager.FEATURE_FACE) } returns true
            every { withBuildConfig.getSdkVersion() } returns Build.VERSION_CODES.P

            assertFalse(androidBiometric.hasFace())
            verify(exactly = 0) { context.packageManager }
            verify(exactly = 0) { packageManager.hasSystemFeature(PackageManager.FEATURE_FACE) }
        }
    }

    @Test
    fun `test getCurrentBiometricTypeInUse return FACE_IN_USE if face and fingerprint are available`() {
        runBlocking {
            prepareIsBiometricAuthAvailable()

            val packageManager = mockk<PackageManager>()
            every { context.packageManager } returns packageManager
            every { packageManager.hasSystemFeature(PackageManager.FEATURE_FACE) } returns true
            every { withBuildConfig.getSdkVersion() } returns Build.VERSION_CODES.Q
            every { packageManager.hasSystemFeature(PackageManager.FEATURE_FINGERPRINT) } returns true


            assertEquals(FACE_IN_USE, androidBiometric.getCurrentBiometricTypeInUse())
        }
    }

    @Test
    fun `test getCurrentBiometricTypeInUse return FACE_IN_USE if only face is available`() {
        runBlocking {
            prepareIsBiometricAuthAvailable()

            val packageManager = mockk<PackageManager>()
            every { context.packageManager } returns packageManager
            every { packageManager.hasSystemFeature(PackageManager.FEATURE_FACE) } returns true
            every { withBuildConfig.getSdkVersion() } returns Build.VERSION_CODES.Q
            every { packageManager.hasSystemFeature(PackageManager.FEATURE_FINGERPRINT) } returns false


            assertEquals(FACE_IN_USE, androidBiometric.getCurrentBiometricTypeInUse())
        }
    }

    @Test
    fun `test getCurrentBiometricTypeInUse return FINGERPRINT_IN_USE if only fingerprint is available`() {
        runBlocking {
            prepareIsBiometricAuthAvailable()

            val packageManager = mockk<PackageManager>()
            every { context.packageManager } returns packageManager
            every { packageManager.hasSystemFeature(PackageManager.FEATURE_FACE) } returns false
            every { withBuildConfig.getSdkVersion() } returns Build.VERSION_CODES.Q
            every { packageManager.hasSystemFeature(PackageManager.FEATURE_FINGERPRINT) } returns true


            assertEquals(Biometric.FINGERPRINT_IN_USE, androidBiometric.getCurrentBiometricTypeInUse())
        }
    }

    @Test
    fun `test getCurrentBiometricTypeInUse return NONE_IN_USE if only face and fingerprint are not available`() {
        runBlocking {
            prepareIsBiometricAuthAvailable()

            val packageManager = mockk<PackageManager>()
            every { context.packageManager } returns packageManager
            every { packageManager.hasSystemFeature(PackageManager.FEATURE_FACE) } returns false
            every { withBuildConfig.getSdkVersion() } returns Build.VERSION_CODES.Q
            every { packageManager.hasSystemFeature(PackageManager.FEATURE_FINGERPRINT) } returns false

            assertEquals(Biometric.NONE_IN_USE, androidBiometric.getCurrentBiometricTypeInUse())
        }
    }

    @Test
    fun `test getCurrentBiometricTypeInUse return NONE_IN_USE if biometry is not available`() {
        runBlocking {
            val biometricManager: BiometricManager = mockk()
            every { biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG) } returns BiometricManager.BIOMETRIC_STATUS_UNKNOWN
            mockkStatic(BiometricManager::class)
            every { BiometricManager.from(context) } returns biometricManager

            assertEquals(Biometric.NONE_IN_USE, androidBiometric.getCurrentBiometricTypeInUse())
        }
    }
}
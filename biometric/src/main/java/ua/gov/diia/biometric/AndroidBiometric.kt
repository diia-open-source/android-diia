package ua.gov.diia.biometric

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.biometric.BiometricManager
import dagger.hilt.android.qualifiers.ApplicationContext
import ua.gov.diia.biometric.Biometric.Companion.FACE_IN_USE
import ua.gov.diia.biometric.Biometric.Companion.FINGERPRINT_IN_USE
import ua.gov.diia.biometric.Biometric.Companion.NONE_IN_USE
import ua.gov.diia.core.util.delegation.WithBuildConfig
import javax.inject.Inject

class AndroidBiometric @Inject constructor(
    @ApplicationContext private val context: Context,
    private val withBuildConfig: WithBuildConfig,
) : Biometric {

    override fun isBiometricAuthAvailable(): Boolean {
        return when (BiometricManager.from(context)
            .canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG)) {
            BiometricManager.BIOMETRIC_SUCCESS -> true
            else -> false
        }
    }

    override fun hasFingerprint(): Boolean {
        return context.packageManager.hasSystemFeature(PackageManager.FEATURE_FINGERPRINT)
    }

    override fun hasFace(): Boolean {
        return if (withBuildConfig.getSdkVersion() >= Build.VERSION_CODES.Q) {
            context.packageManager.hasSystemFeature(PackageManager.FEATURE_FACE)
        } else {
            false
        }
    }

    override fun getCurrentBiometricTypeInUse(): Int {
        return if (isBiometricAuthAvailable()) {
            val faceAvailable = hasFace()
            val fingerprintAvailable = hasFingerprint()
            if (faceAvailable && fingerprintAvailable) {
                FACE_IN_USE
            } else if (faceAvailable) {
                FACE_IN_USE
            } else if (fingerprintAvailable) {
                FINGERPRINT_IN_USE
            } else {
                NONE_IN_USE
            }
        } else {
            NONE_IN_USE
        }
    }

    override fun getPromptDescriptionString(): Int {
        val type = getCurrentBiometricTypeInUse()
        return if (type == FACE_IN_USE) {
            R.string.use_face_id
        } else {
            R.string.use_touch_id
        }
    }
}
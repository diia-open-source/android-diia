package ua.gov.diia.biometric.ui

import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import ua.gov.diia.biometric.R

fun Fragment.showBiometricAuthPrompt(
    descriptionTextRes: Int,
    callback: (Boolean) -> Unit,
) {
    val promptCallback = object : BiometricPrompt.AuthenticationCallback() {

        override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
            super.onAuthenticationSucceeded(result)
            callback(true)
        }

        override fun onAuthenticationFailed() {
            super.onAuthenticationFailed()
            callback(false)
        }

        override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
            super.onAuthenticationError(errorCode, errString)
            callback(false)
        }
    }
    val executor = ContextCompat.getMainExecutor(requireContext())
    val prompt = BiometricPrompt(this, executor, promptCallback)
    val promptInfo = BiometricPrompt.PromptInfo.Builder()
        .setTitle(getString(R.string.touch_id_for_diia))
        .setDescription(getString(descriptionTextRes))
        .setNegativeButtonText(getString(R.string.cancel))
        .build()

    prompt.authenticate(promptInfo)
}
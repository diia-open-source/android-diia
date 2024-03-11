package ua.gov.diia.opensource.helper

import androidx.fragment.app.Fragment
import ua.gov.diia.biometric.Biometric
import ua.gov.diia.biometric.ui.showBiometricAuthPrompt
import ua.gov.diia.core.util.extensions.fragment.navigate
import ua.gov.diia.opensource.NavMainXmlDirections
import ua.gov.diia.opensource.repository.settings.AppSettingsRepository
import ua.gov.diia.pin.helper.PinHelper
import ua.gov.diia.pin.ui.input.AlternativeAuthCallback
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PinHelperImpl @Inject constructor(
    private val biometric: Biometric,
    private val settingsRepository: AppSettingsRepository,
) : PinHelper {

    override fun isAlternativeAuthAvailable(): Boolean {
        return biometric.isBiometricAuthAvailable()
    }

    override suspend fun isAlternativeAuthEnabled(): Boolean {
        return biometric.isBiometricAuthAvailable() && settingsRepository.isBiometricAuthEnabled()
    }

    override fun openAlternativeAuth(host: Fragment, callback: AlternativeAuthCallback) {
        host.showBiometricAuthPrompt(biometric.getPromptDescriptionString()) { result ->
            if (result) {
                callback.onAlternativeAuthSuccessful()
            } else {
                callback.onAlternativeAuthFailed()
            }
        }
    }

    override fun navigateToAlternativeAuthSetup(
        host: Fragment,
        resultDestinationId: Int,
        resultKey: String,
        pin: String,
    ) {
        host.navigate(
            NavMainXmlDirections.actionGlobalToSetupBiometric(
                resultDestinationId = resultDestinationId,
                resultKey = resultKey,
                pin = pin,
            )
        )
    }
}
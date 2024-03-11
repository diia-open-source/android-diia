package ua.gov.diia.opensource.helper

import androidx.fragment.app.Fragment
import ua.gov.diia.core.util.extensions.fragment.navigate
import ua.gov.diia.pin.NavPinCreateDirections
import ua.gov.diia.pin.model.CreatePinFlowType
import ua.gov.diia.pin.repository.LoginPinRepository
import ua.gov.diia.splash.helper.SplashHelper
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SplashHelperImpl @Inject constructor(
    private val loginPinRepository: LoginPinRepository,
) : SplashHelper {

    override suspend fun isProtectionExists(): Boolean {
        return loginPinRepository.isPinPresent()
    }

    override suspend fun setUserAuthorized(protectionKey: String) {
        loginPinRepository.setUserAuthorized(protectionKey)
    }

    override fun navigateToProtectionCreation(
        host: Fragment,
        resultDestinationId: Int,
        resultKey: String
    ) {
        host.navigate(
            NavPinCreateDirections.actionGlobalCreatePin(
                resultDestinationId = resultDestinationId,
                resultKey = resultKey,
                flowType = CreatePinFlowType.AUTHORIZATION
            )
        )
    }
}
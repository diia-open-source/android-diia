package ua.gov.diia.verification.ui.controller

import androidx.lifecycle.asFlow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import ua.gov.diia.core.util.alert.ClientAlertDialogsFactory
import ua.gov.diia.core.util.delegation.WithErrorHandling
import ua.gov.diia.core.util.delegation.WithRetryLastAction
import ua.gov.diia.core.util.system.application.ApplicationLauncher
import ua.gov.diia.core.util.system.application.InstalledApplicationInfoProvider
import ua.gov.diia.core.util.system.service.SystemServiceProvider
import ua.gov.diia.verification.model.VerificationResult
import ua.gov.diia.verification.network.ApiVerification
import ua.gov.diia.verification.ui.methods.VerificationMethod

class TestVerificationControllerVM(
    apiVerification: ApiVerification,
    clientAlertDialogsFactory: ClientAlertDialogsFactory,
    applicationInfoProvider: InstalledApplicationInfoProvider,
    systemServiceProvider: SystemServiceProvider,
    applicationLauncher: ApplicationLauncher,
    retryErrorBehavior: WithRetryLastAction,
    errorHandlingBehaviour: WithErrorHandling,
    verificationMethods: Map<String, @JvmSuppressWildcards VerificationMethod>,
    private val onVerificationCompleted: MutableSharedFlow<VerificationResult>,
) : VerificationControllerVM(
    apiVerification,
    clientAlertDialogsFactory,
    applicationInfoProvider,
    systemServiceProvider,
    applicationLauncher,
    retryErrorBehavior,
    errorHandlingBehaviour,
    verificationMethods
) {

    val preferredVerificationMethodCodeVar: String?
        get() = preferredVerificationMethodCode

    val authMethodSkippedVar
        get() = authMethodsSkipped

    val verifyingUserValue: Boolean
        get() = verifyingUser.value ?: false

    override fun doOnVerificationCompleted(result: VerificationResult) {
        runBlocking { onVerificationCompleted.emit(result) }
    }

    suspend fun awaitUserVerifying() {
        verifyingUser.asFlow().first { !it }
    }
}
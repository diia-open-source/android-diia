package ua.gov.diia.verification.ui.methods

import ua.gov.diia.verification.model.VerificationUrl

data class VerificationRequest(
    val navRequest: VerificationNavRequest? = null,
    val url: VerificationUrl? = null,
    val shouldLaunchUrl: Boolean = false,
)
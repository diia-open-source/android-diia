package ua.gov.diia.verification.util

import com.nhaarman.mockitokotlin2.mock
import ua.gov.diia.verification.model.VerificationUrl
import ua.gov.diia.verification.ui.methods.VerificationMethod
import ua.gov.diia.verification.ui.methods.VerificationRequest

class StubVerificationMethod(
    private val isUsesUrl: Boolean,
    override val name: String = "test",
    override val isAvailable: Boolean = true,
    var authUrl: String = "http://test.com/1329"
) : VerificationMethod() {

    override val iconResId = 0
    override val titleResId = 0
    override val descriptionResId = 0

    override suspend fun getVerificationRequest(
        verificationSchema: String,
        processId: String
    ) = VerificationRequest(
        navRequest = { _, _ -> mock() },
        url = if (isUsesUrl) {
            VerificationUrl(authUrl, "tk2", null)
        } else {
            null
        },
        shouldLaunchUrl = isUsesUrl
    )
}

package ua.gov.diia.verification.model

sealed class VerificationResult {

    data class Common(val processId: String) : VerificationResult()

    data class GenerateSignature(
        val resourceId: String,
        val flowId: String
    ) : VerificationResult()
}

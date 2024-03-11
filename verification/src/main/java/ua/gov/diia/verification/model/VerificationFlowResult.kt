package ua.gov.diia.verification.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

sealed class VerificationFlowResult : Parcelable {

    @Parcelize
    data class VerificationMethod(val method: String) : VerificationFlowResult()

    @Parcelize
    data class CompleteVerificationStep(
        val requestId: String? = null,
        val bankCode: String? = null //used for bankId verification
    ) : VerificationFlowResult()
}

package ua.gov.diia.documents.verificationdata

import ua.gov.diia.ui_base.components.organism.document.VerificationCodesOrgData

sealed class DocumentVerificationDataResult(open val verificationCodesOrgData: VerificationCodesOrgData) {

    data class DocumentVerificationDataSuccessfulLoadResult(override val verificationCodesOrgData: VerificationCodesOrgData) :
        DocumentVerificationDataResult(verificationCodesOrgData)

    data class DocumentVerificationDataErrorLoadResult(
        override val verificationCodesOrgData: VerificationCodesOrgData,
        val exception: Exception,
        val code: Int? = null
    ) :
        DocumentVerificationDataResult(verificationCodesOrgData)
}
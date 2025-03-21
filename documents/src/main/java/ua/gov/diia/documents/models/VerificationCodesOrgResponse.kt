package ua.gov.diia.documents.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import ua.gov.diia.core.models.common_compose.org.verification.VerificationCodesOrg

@JsonClass(generateAdapter = true)
data class VerificationCodesOrgResponse(
    @Json(name = "verificationCodesOrg")
    val verificationCodesOrg: VerificationCodesOrg?
)
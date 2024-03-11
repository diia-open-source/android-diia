package ua.gov.diia.verification.model


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import ua.gov.diia.core.models.dialogs.TemplateDialogModel

@JsonClass(generateAdapter = true)
data class VerificationUrl(
    @Json(name = "authUrl")
    val authUrl: String?,
    @Json(name = "token")
    val token: String?,
    @Json(name = "template")
    val template: TemplateDialogModel?
)
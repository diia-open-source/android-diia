package ua.gov.diia.ps_criminal_cert.models.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import ua.gov.diia.core.models.dialogs.TemplateDialogModel

@JsonClass(generateAdapter = true)
data class CriminalCertContacts(
    @Json(name = "title")
    val title: String?,
    @Json(name = "text")
    val text: String?,
    @Json(name = "phoneNumber")
    val phoneNumber: String?,
    @Json(name = "email")
    val email: String?,
    @Json(name = "template")
    val template: TemplateDialogModel? = null
)
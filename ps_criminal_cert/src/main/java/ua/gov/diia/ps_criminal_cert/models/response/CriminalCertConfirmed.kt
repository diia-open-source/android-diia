package ua.gov.diia.ps_criminal_cert.models.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import ua.gov.diia.core.models.common.NavigationPanel
import ua.gov.diia.core.models.dialogs.TemplateDialogModel

@JsonClass(generateAdapter = true)
data class CriminalCertConfirmed(
    @Json(name = "applicationId")
    val applicationId: String?,
    @Json(name = "processCode")
    val processCode: Int?,
    @Json(name = "template")
    val template: TemplateDialogModel?,
    @Json(name = "navigationPanel")
    val navigationPanel: NavigationPanel?,
)
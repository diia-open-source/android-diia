package ua.gov.diia.ps_criminal_cert.models.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import ua.gov.diia.core.models.dialogs.TemplateDialogModel

@JsonClass(generateAdapter = true)
data class CriminalCertFileData(
    @Json(name = "file")
    val file: String?,
    @Json(name = "template")
    val template: TemplateDialogModel?,
)
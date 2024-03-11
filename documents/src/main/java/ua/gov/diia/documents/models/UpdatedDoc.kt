package ua.gov.diia.documents.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import ua.gov.diia.core.models.dialogs.TemplateDialogModel

@JsonClass(generateAdapter = true)
data class UpdatedDoc(
    @Json(name = "educationDocument")
    val educationDocument: DiiaDocument?,
    @Json(name = "processCode")
    val processCode: String?,
    @Json(name = "template")
    val template: TemplateDialogModel?
)
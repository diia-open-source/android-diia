package ua.gov.diia.core.models.common

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import ua.gov.diia.core.models.dialogs.TemplateDialogModel

@JsonClass(generateAdapter = true)
data class DetailsPDF(
    @Json(name = "documentFile")
    val documentFile: DocumentFile?,
    @Json(name = "processCode")
    val processCode: Long?,
    @Json(name = "template")
    val template: TemplateDialogModel?
)

@JsonClass(generateAdapter = true)
data class DocumentFile(
    @Json(name = "file")
    val file: String,
    @Json(name = "name")
    val name: String,
    @Json(name = "mimeType")
    val mimeType: String
)
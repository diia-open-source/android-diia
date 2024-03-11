package ua.gov.diia.core.models.dialogs

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class TemplateDialogModelWithProcessCode(
    @Json(name = "processCode")
    val processCode: Int?,
    @Json(name = "template")
    val template: TemplateDialogModel
)

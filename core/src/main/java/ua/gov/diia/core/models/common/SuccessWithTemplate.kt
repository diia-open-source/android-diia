package ua.gov.diia.core.models.common

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import ua.gov.diia.core.models.dialogs.TemplateDialogModel

//todo asap move to core
@JsonClass(generateAdapter = true)
class SuccessWithTemplate(
    @Json(name = "success")
    val success: Boolean?,
    @Json(name = "template")
    val template: TemplateDialogModel?
)
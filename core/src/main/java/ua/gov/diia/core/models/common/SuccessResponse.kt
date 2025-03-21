package ua.gov.diia.core.models.common

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import ua.gov.diia.core.models.dialogs.TemplateDialogModel

@JsonClass(generateAdapter = true)
data class SuccessResponse(
    @Json(name = "success")
    val success: Boolean,
    @Json(name = "processCode")
    val processCode: Long?,
    @Json(name = "template")
    val template: TemplateDialogModel?
)
package ua.gov.diia.core.models.common_compose.general

import com.squareup.moshi.Json
import ua.gov.diia.core.models.dialogs.TemplateDialogModel

data class DiiaPostResponse(
    @Json(name = "nextStep")
    val nextStep: String?,
    @Json(name = "processCode")
    val processCode: Long?,
    @Json(name = "template")
    val template: TemplateDialogModel?,
    @Json(name = "applicationId")
    val applicationId: String?
)
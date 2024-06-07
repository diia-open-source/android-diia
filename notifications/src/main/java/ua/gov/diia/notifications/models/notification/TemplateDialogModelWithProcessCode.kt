package ua.gov.diia.notifications.models.notification

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import ua.gov.diia.core.models.dialogs.TemplateDialogModel

@JsonClass(generateAdapter = true)
class TemplateDialogModelWithProcessCode(
    @Json(name = "processCode")
    val processCode: Int?,
    @Json(name = "template")
    val template: TemplateDialogModel?
)

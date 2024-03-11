package ua.gov.diia.notifications.models.notification

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import ua.gov.diia.core.models.dialogs.TemplateDialogModel

@JsonClass(generateAdapter = true)
data class SubscribeResponse(
    @Json(name = "success")
    val success: Boolean?,
    @Json(name = "template")
    val template: TemplateDialogModel?
)
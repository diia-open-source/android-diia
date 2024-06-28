package ua.gov.diia.core.models.notification.pull.message


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import ua.gov.diia.core.models.common_compose.general.Body
import ua.gov.diia.core.models.common_compose.general.BottomGroup
import ua.gov.diia.core.models.common_compose.general.TopGroup
import ua.gov.diia.core.models.dialogs.TemplateDialogModel

@JsonClass(generateAdapter = true)
data class NotificationFull(
    @Json(name = "body")
    val body: List<Body>?,
    @Json(name = "bottomGroup")
    val bottomGroup: List<BottomGroup>?,
    @Json(name = "topGroup")
    val topGroup: List<TopGroup>?,
    @Json(name = "template")
    val template: TemplateDialogModel?,
    @Json(name = "processCode")
    val processCode: Long?,
)
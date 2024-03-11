package ua.gov.diia.core.models.common_compose.general


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import ua.gov.diia.core.models.dialogs.TemplateDialogModel

@JsonClass(generateAdapter = true)
data class DiiaResponse(
    @Json(name = "body")
    val body: List<Body>?,
    @Json(name = "processCode")
    val processCode: String?,
    @Json(name = "template")
    val template: TemplateDialogModel?,
    @Json(name = "topGroup")
    val topGroup: List<TopGroup>?,
    @Json(name = "bottomGroup")
    val bottomGroup: List<BottomGroup>?,
)
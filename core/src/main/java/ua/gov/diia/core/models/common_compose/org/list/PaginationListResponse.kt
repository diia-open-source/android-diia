package ua.gov.diia.core.models.common_compose.org.list

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import ua.gov.diia.core.models.common_compose.mlc.text.StubMessageMlc
import ua.gov.diia.core.models.dialogs.TemplateDialogModel

@JsonClass(generateAdapter = true)
data class PaginationListResponse(
    @Json(name = "items")
    val items: List<PaginationItem>?,
    @Json(name = "total")
    val total: Int?,
    @Json(name = "processCode")
    val processCode: String?,
    @Json(name = "template")
    val template: TemplateDialogModel?,
    @Json(name = "stubMessageMlc")
    val stubMessageMlc: StubMessageMlc?
)
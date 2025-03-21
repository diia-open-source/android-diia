package ua.gov.diia.core.models.common_compose.org.media

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import ua.gov.diia.core.models.document.docgroups.v2.IconRight

@JsonClass(generateAdapter = true)
data class ListItemUploadMlc(
    @Json(name = "componentId")
    val componentId: String?,
    @Json(name = "id")
    val id: String,
    @Json(name = "label")
    val label: String,
    @Json(name = "description")
    val description: String?,
    @Json(name = "errorDescription")
    val errorDescription: String?,
    @Json(name = "errorType")
    val errorType: String?,
    @Json(name = "iconRight")
    val iconRight: IconRight?,
    @Json(name = "iconRightContentDescription")
    val iconRightContentDescription: String?
)
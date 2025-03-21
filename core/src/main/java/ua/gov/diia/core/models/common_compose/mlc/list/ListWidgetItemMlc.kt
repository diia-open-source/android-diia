package ua.gov.diia.core.models.common_compose.mlc.list

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import ua.gov.diia.core.models.document.docgroups.v2.IconLeft
import ua.gov.diia.core.models.document.docgroups.v2.IconRight

@JsonClass(generateAdapter = true)
data class ListWidgetItemMlc(
    @Json(name = "componentId")
    val componentId: String? = null,
    @Json(name = "id")
    val id: String?,
    @Json(name = "label")
    val label: String,
    @Json(name = "description")
    val description: String?,
    @Json(name = "iconLeft")
    val iconLeft: IconLeft?,
    @Json(name = "iconRight")
    val iconRight: IconRight?,
    @Json(name = "logoLeft")
    val logoLeft: String?,
    @Json(name = "state")
    val state: String?,
    @Json(name = "dataJson")
    val dataJson: String?
)

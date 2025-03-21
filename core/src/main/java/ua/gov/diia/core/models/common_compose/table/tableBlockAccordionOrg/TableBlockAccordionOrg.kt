package ua.gov.diia.core.models.common_compose.table.tableBlockAccordionOrg


import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize
import ua.gov.diia.core.models.common_compose.table.Item

@Parcelize
@JsonClass(generateAdapter = true)
data class TableBlockAccordionOrg(
    @Json(name = "heading")
    val heading: String?,
    @Json(name = "isOpen")
    val isOpen: Boolean?,
    @Json(name = "componentId")
    val componentId: String? = null,
    @Json(name = "items")
    val items: List<Item>? = null,
): Parcelable
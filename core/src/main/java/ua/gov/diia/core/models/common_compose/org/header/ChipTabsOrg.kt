package ua.gov.diia.core.models.common_compose.org.header

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ChipTabsOrg(
    @Json(name = "items")
    val items: List<Item>,
    @Json(name = "label")
    val label: String?,
    @Json(name = "preselectedCode")
    val preselectedCode: String
) {
    @JsonClass(generateAdapter = true)
    data class Item(
        @Json(name = "code")
        val code: String?,
        @Json(name = "count")
        val count: String,
        @Json(name = "label")
        val label: String
    )
}
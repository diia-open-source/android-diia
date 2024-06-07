package ua.gov.diia.core.models.common_compose.org.list


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import ua.gov.diia.core.models.common_compose.mlc.list.ListItemMlc

@JsonClass(generateAdapter = true)
data class ListItemGroupOrg(
    @Json(name = "items")
    val items: List<ListItemMlc>,
    @Json(name = "title?")
    val title: String?,
    @Json(name = "componentId")
    val componentId: String? = null,
)
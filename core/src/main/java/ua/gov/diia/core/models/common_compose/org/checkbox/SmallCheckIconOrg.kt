package ua.gov.diia.core.models.common_compose.org.checkbox

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SmallCheckIconOrg(
    @Json(name = "componentId")
    val componentId: String?,
    @Json(name = "inputCode")
    val inputCode: String?,
    @Json(name = "id")
    val id: String?,
    @Json(name = "title")
    val title: String?,
    @Json(name = "items")
    val items: List<SmallCheckIconItem>
)

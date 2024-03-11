package ua.gov.diia.core.models.common.message

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class StubMessage(
    @Json(name = "icon")
    val icon: String?,
    @Json(name = "text")
    val text: String?,
    @Json(name = "canRepeat")
    val canRepeat: Boolean?,
    @Json(name = "title")
    val title: String?,
    @Json(name = "description")
    val description: String?
)

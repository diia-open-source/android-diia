package ua.gov.diia.core.models.common.message


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import ua.gov.diia.core.models.common.message.TextParameter

@JsonClass(generateAdapter = true)
data class StatusMessageParameterized(
    @Json(name = "icon")
    val icon: String?,
    @Json(name = "parameters")
    val parameters: List<TextParameter>?,
    @Json(name = "text")
    val text: String?,
    @Json(name = "title")
    val title: String?
)
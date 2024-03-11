package ua.gov.diia.core.models.notification.pull.message


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Data(
    @Json(name = "alt")
    val alt: String?,
    @Json(name = "name")
    val name: String?,
    @Json(name = "resource")
    val resource: String?
)
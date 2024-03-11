package ua.gov.diia.core.models.appversion

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AppSettingsInfo(
    @Json(name = "needActions")
    val actions: List<String>?
)
package ua.gov.diia.core.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PushToken(
    @Json(name = "pushToken")
    val pushToken: String
)
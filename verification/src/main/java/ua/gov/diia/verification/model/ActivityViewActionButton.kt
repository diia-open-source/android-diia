package ua.gov.diia.verification.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ActivityViewActionButton(
    @Json(name = "action")
    val action: String
)
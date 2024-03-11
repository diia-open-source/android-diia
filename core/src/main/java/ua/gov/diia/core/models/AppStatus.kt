package ua.gov.diia.core.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AppStatus(
    @Json(name = "lastActivityDate")
    val lastActivityDate : String?,
    @Json(name = "lastDocumentUpdate")
    val lastDocumentUpdate : String? = null
)

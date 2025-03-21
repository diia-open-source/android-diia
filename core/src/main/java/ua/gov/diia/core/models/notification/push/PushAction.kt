package ua.gov.diia.core.models.notification.push

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PushAction(
    @Json(name = "resourceId")
    val resourceId: String?,
    @Json(name = "type")
    val type: String,
    @Json(name = "subtype")
    val subtype: String?,
)
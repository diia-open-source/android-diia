package ua.gov.diia.core.models.common


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SuccessCheck(
    @Json(name = "success")
    val success: Boolean?
)
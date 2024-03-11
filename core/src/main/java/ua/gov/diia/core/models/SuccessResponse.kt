package ua.gov.diia.core.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SuccessResponse(
    val success: Boolean
)
package ua.gov.diia.documents.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class QRUrl(
    @Json(name = "id")
    val id: String?,
    @Json(name = "link")
    val link: String,
    @Json(name = "barcode")
    val shareCode: String?,
    @Json(name = "timerText")
    val timerText: String,
    @Json(name = "timerTime")
    val timerTime: Int
)
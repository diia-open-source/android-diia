package ua.gov.diia.core.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ITN(
    @Json(name = "birthDay")
    val birthDay: String?,
    @Json(name = "currentDate")
    val currentDate: String?,
    @Json(name = "expirationDate")
    val expirationDate: String?,
    @Json(name = "fName")
    val fName: String?,
    @Json(name = "itn")
    val itn: String?,
    @Json(name = "lName")
    val lName: String?,
    @Json(name = "mName")
    val mName: String?
)
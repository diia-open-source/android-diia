package ua.gov.diia.bankid.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AuthBank(
    @Json(name = "id")
    val id: String?,
    @Json(name = "logoUrl")
    val logoUrl: String?,
    @Json(name = "name")
    val name: String?,
    @Json(name = "workable")
    val workable: Boolean?
)
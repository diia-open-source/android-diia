package ua.gov.diia.login.model


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LoginToken(
    @Json(name = "token")
    val token: String
)
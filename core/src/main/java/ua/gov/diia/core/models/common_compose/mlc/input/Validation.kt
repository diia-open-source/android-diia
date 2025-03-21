package ua.gov.diia.core.models.common_compose.mlc.input


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Validation(
    @Json(name = "errorMessage")
    val errorMessage: String,
    @Json(name = "flags")
    val flags: List<String>,
    @Json(name = "regexp")
    val regexp: String
)
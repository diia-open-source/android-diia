package ua.gov.diia.address_search.models


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AddressFieldRequestValue(
    @Json(name = "id")
    val id: String?,
    @Json(name = "type")
    val type: String?,
    @Json(name = "value")
    val value: String?
)
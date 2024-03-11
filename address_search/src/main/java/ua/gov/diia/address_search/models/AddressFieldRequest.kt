package ua.gov.diia.address_search.models


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AddressFieldRequest(
    @Json(name = "values")
    val values: List<AddressFieldRequestValue>? = null
)
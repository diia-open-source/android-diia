package ua.gov.diia.publicservice.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PublicServicesCategories(
    @Json(name = "publicServicesCategories")
    val categories: List<PublicServiceCategory>,
    @Json(name = "tabs")
    val tabs: List<PublicServiceTab>
)

package ua.gov.diia.documents.models.share

import com.squareup.moshi.Json

data class BirthCertShareData(
    @Json(name = "serie")
    val serie: String,
    @Json(name = "number")
    val number: String
)

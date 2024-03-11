package ua.gov.diia.ps_criminal_cert.models.enums

import com.squareup.moshi.Json

enum class CriminalCertType {
    @Json(name = "full")
    FULL,

    @Json(name = "short")
    SHORT
}
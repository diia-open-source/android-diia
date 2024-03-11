package ua.gov.diia.ps_criminal_cert.models.enums

import com.squareup.moshi.Json

enum class CriminalCertScreen {
    @Json(name = "birthPlace")
    BIRTH_PLACE,

    @Json(name = "nationalities")
    NATIONALITIES,

    @Json(name = "registrationPlace")
    REGISTRATION_PLACE,

    @Json(name = "contacts")
    CONTACTS
}
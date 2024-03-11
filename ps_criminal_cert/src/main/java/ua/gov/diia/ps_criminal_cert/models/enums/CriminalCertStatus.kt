package ua.gov.diia.ps_criminal_cert.models.enums

import com.squareup.moshi.Json

enum class CriminalCertStatus(val str: String) {
    @Json(name = "applicationProcessing")
    PROCESSING("applicationProcessing"),

    @Json(name = "done")
    DONE("done")
}
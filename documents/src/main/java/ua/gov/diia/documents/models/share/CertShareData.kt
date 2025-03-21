package ua.gov.diia.documents.models.share

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CertShareData(
    @Json(name = "unmarriedCertificate")
    val unmarriedCertificate: UnCerShareData? = null,
    @Json(name = "birthCertificate")
    val birthCertificate: BirthCertShareData? = null
)

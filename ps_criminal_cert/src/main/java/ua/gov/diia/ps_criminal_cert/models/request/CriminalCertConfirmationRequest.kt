package ua.gov.diia.ps_criminal_cert.models.request

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import ua.gov.diia.ps_criminal_cert.models.enums.CriminalCertType

@JsonClass(generateAdapter = true)
data class CriminalCertConfirmationRequest(
    @Json(name = "reasonId")
    val reasonId: String?,
    @Json(name = "certificateType")
    val certificateType: CriminalCertType?,
    @Json(name = "previousFirstName")
    val previousFirstName: String?,
    @Json(name = "previousMiddleName")
    val previousMiddleName: String?,
    @Json(name = "previousLastName")
    val previousLastName: String?,
    @Json(name = "birthPlace")
    val birthPlace: BirthPlace?,
    @Json(name = "nationalities")
    val nationalities: List<String>?,
    @Json(name = "registrationAddressId")
    val registrationAddressId: String?,
    @Json(name = "phoneNumber")
    val phoneNumber: String?,
    @Json(name = "email")
    val email: String? = null,
    @Json(name = "publicService")
    val publicService: PublicService?
) {
    @JsonClass(generateAdapter = true)
    data class BirthPlace(
        @Json(name = "country")
        val country: String?,
        @Json(name = "city")
        val city: String?
    )
}
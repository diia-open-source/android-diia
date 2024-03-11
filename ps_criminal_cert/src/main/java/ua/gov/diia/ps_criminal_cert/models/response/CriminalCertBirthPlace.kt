package ua.gov.diia.ps_criminal_cert.models.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import ua.gov.diia.ps_criminal_cert.models.enums.CriminalCertScreen
import ua.gov.diia.core.models.dialogs.TemplateDialogModel

@JsonClass(generateAdapter = true)
data class CriminalCertBirthPlace(
    @Json(name = "birthPlaceDataScreen")
    val data: BirthPlaceDataScreen?,
    @Json(name = "template")
    val template: TemplateDialogModel? = null,
) {

    @JsonClass(generateAdapter = true)
    data class BirthPlaceDataScreen(
        @Json(name = "title")
        val title: String?,
        @Json(name = "country")
        val country: Country?,
        @Json(name = "city")
        val city: City?,
        @Json(name = "nextScreen")
        val nextScreen: CriminalCertScreen?
    )

    @JsonClass(generateAdapter = true)
    data class Country(
        @Json(name = "label")
        val label: String,
        @Json(name = "value")
        val value: String?,
        @Json(name = "hint")
        val hint: String,
        @Json(name = "checkbox")
        val checkbox: String?,
        @Json(name = "otherCountry")
        val otherCountry: OtherCountry?
    )

    @JsonClass(generateAdapter = true)
    data class City(
        @Json(name = "label")
        val label: String,
        @Json(name = "hint")
        val hint: String,
        @Json(name = "description")
        val description: String?
    )

    @JsonClass(generateAdapter = true)
    data class OtherCountry(
        @Json(name = "label")
        val label: String,
        @Json(name = "hint")
        val hint: String
    )
}
package ua.gov.diia.documents.models.docgroups.v2


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class DocData(
    @Json(name = "birthday")
    val birthday: String?,
    @Json(name = "category")
    val category: String?,
    @Json(name = "docName")
    val docName: String?,
    @Json(name = "docType")
    val docType: String?,
    @Json(name = "expirationDate")
    val expirationDate: String?,
    @Json(name = "formOfEducation")
    val formOfEducation: String?,
    @Json(name = "fullName")
    val fullName: String?,
    @Json(name = "fullNameEN")
    val fullNameEN: String?,
    @Json(name = "licenceNumber")
    val licenceNumber: String?,
    @Json(name = "organisation")
    val organisation: String?,
    @Json(name = "pensionType")
    val pensionType: String?,
    @Json(name = "rnokpp")
    val rnokpp: String?
)
package ua.gov.diia.documents.models.docgroups.v2

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class DocData(
    @Json(name = "name")
    val name: String? = null,
    @Json(name = "birthday")
    val birthday: String? = null,
    @Json(name = "category")
    val category: String? = null,
    @Json(name = "docName")
    val docName: String? = null,
    @Json(name = "docType")
    val docType: String? = null,
    @Json(name = "expirationDate")
    val expirationDate: String? = null,
    @Json(name = "formOfEducation")
    val formOfEducation: String? = null,
    @Json(name = "fullName")
    val fullName: String? = null,
    @Json(name = "fullNameEN")
    val fullNameEN: String? = null,
    @Json(name = "licenceNumber")
    val licenceNumber: String? = null,
    @Json(name = "organisation")
    val organisation: String? = null,
    @Json(name = "pensionType")
    val pensionType: String? = null,
    @Json(name = "rnokpp")
    val rnokpp: String? = null,
    @Json(name = "validUntil")
    val validUntil: String? = null,
    @Json(name = "updateDate")
    val updateDate: String? = null,
    @Json(name = "owner")
    val owner: String? = null,
    @Json(name = "vin")
    val vin: String? = null,
    @Json(name = "model")
    val model: String? = null,
    @Json(name = "licensePlate")
    val licensePlate: String? = null,
    @Json(name = "mark")
    val mark: String? = null,
    @Json(name = "properUser")
    val properUser: String? = null,
    @Json(name = "birthPlace")
    val birthPlace: String? = null,
    @Json(name = "certificateIssuer")
    val certificateIssuer: String? = null,
    @Json(name = "dataIssued")
    val dataIssued: String? = null,
    @Json(name = "disease")
    val disease: String? = null,
    @Json(name = "educationInstitutionName")
    val educationInstitutionName: String? = null,
    @Json(name = "firstPositiveTime")
    val firstPositiveTime: String? = null,
    @Json(name = "isBooster")
    val isBooster: Boolean? = null,
    @Json(name = "invalidGroup")
    val invalidGroup: String? = null,
    @Json(name = "member")
    val member: String? = null,
    @Json(name = "numberVaccine")
    val numberVaccine: String? = null,
    @Json(name = "result")
    val result: String? = null,
    @Json(name = "resultTestTime")
    val resultTestTime: String? = null,
    @Json(name = "sampleCollectionTime")
    val sampleCollectionTime: String? = null,
    @Json(name = "serialNumber")
    val serialNumber: String? = null,
    @Json(name = "status")
    val status: String? = null,
    @Json(name = "testCenter")
    val testCenter: String? = null,
    @Json(name = "testManufacturer")
    val testManufacturer: String? = null,
    @Json(name = "testName")
    val testName: String? = null,
    @Json(name = "testType")
    val testType: String? = null,
    @Json(name = "vaccinationDate")
    val vaccinationDate: String? = null,
    @Json(name = "vaccine")
    val vaccine: String? = null,
    @Json(name = "vaccineHolder")
    val vaccineHolder: String? = null,
    @Json(name = "vaccineProduct")
    val vaccineProduct: String? = null,
    @Json(name = "vehicleLicenseId")
    val vehicleLicenseId: String? = null,
    @Json(name = "number")
    val number: String? = null,
    @Json(name = "serial")
    val serial: String? = null,
    @Json(name = "ownerType")
    val ownerType: String? = null,
    @Json(name = "properUserExpirationDate")
    val properUserExpirationDate: String? = null,

): Parcelable
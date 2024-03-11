package ua.gov.diia.ps_criminal_cert.models.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import ua.gov.diia.core.models.common.message.AttentionMessageParameterized
import ua.gov.diia.core.models.dialogs.TemplateDialogModel

@JsonClass(generateAdapter = true)
data class CriminalCertConfirmation(
    @Json(name = "application")
    val application: Application?,
    @Json(name = "processCode")
    val processCode: Int?,
    @Json(name = "template")
    val template: TemplateDialogModel?
) {

    @JsonClass(generateAdapter = true)
    data class Application(
        @Json(name = "title")
        val title: String?,
        @Json(name = "attentionMessage")
        val attentionMessage: AttentionMessageParameterized?,
        @Json(name = "applicant")
        val applicant: Applicant?,
        @Json(name = "contacts")
        val contacts: Contacts?,
        @Json(name = "certificateType")
        val certificateType: CertificateType?,
        @Json(name = "reason")
        val reason: Reason?,
        @Json(name = "checkboxName")
        val checkboxName: String?,
    )

    @JsonClass(generateAdapter = true)
    data class Applicant(
        @Json(name = "title")
        val title: String?,
        @Json(name = "fullName")
        val fullName: LabelValuePair?,
        @Json(name = "previousFirstName")
        val previousFirstName: LabelValuePair?,
        @Json(name = "previousLastName")
        val previousLastName: LabelValuePair?,
        @Json(name = "previousMiddleName")
        val previousMiddleName: LabelValuePair?,
        @Json(name = "gender")
        val gender: LabelValuePair?,
        @Json(name = "nationality")
        val nationality: LabelValuePair?,
        @Json(name = "birthDate")
        val birthDate: LabelValuePair?,
        @Json(name = "birthPlace")
        val birthPlace: LabelValuePair?,
        @Json(name = "registrationAddress")
        val registrationAddress: LabelValuePair?,
    )

    @JsonClass(generateAdapter = true)
    data class Contacts(
        @Json(name = "title")
        val title: String?,
        @Json(name = "phoneNumber")
        val phoneNumber: LabelValuePair?
    )

    @JsonClass(generateAdapter = true)
    data class CertificateType(
        @Json(name = "title")
        val title: String?,
        @Json(name = "type")
        val type: String?
    )

    @JsonClass(generateAdapter = true)
    data class Reason(
        @Json(name = "title")
        val title: String?,
        @Json(name = "reason")
        val reason: String?
    )

    @JsonClass(generateAdapter = true)
    data class LabelValuePair(
        @Json(name = "label")
        val label: String,
        @Json(name = "value")
        val value: String
    )
}
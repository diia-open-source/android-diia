package ua.gov.diia.ps_criminal_cert.models.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import ua.gov.diia.ps_criminal_cert.models.enums.CriminalCertType
import ua.gov.diia.core.models.dialogs.TemplateDialogModel

@JsonClass(generateAdapter = true)
data class CriminalCertTypes(
    @Json(name = "title")
    val title: String?,
    @Json(name = "subtitle")
    val subtitle: String?,
    @Json(name = "types")
    val types: List<Type>?,
    @Json(name = "template")
    val template: TemplateDialogModel? = null
) {

    @JsonClass(generateAdapter = true)
    data class Type(
        @Json(name = "code")
        val code: CriminalCertType,
        @Json(name = "name")
        val name: String,
        @Json(name = "description")
        val description: String,

        val isSelected: Boolean = false
    )
}
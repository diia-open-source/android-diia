package ua.gov.diia.ps_criminal_cert.models.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import ua.gov.diia.core.models.dialogs.TemplateDialogModel

@JsonClass(generateAdapter = true)
data class CriminalCertReasons(
    @Json(name = "title")
    val title: String?,
    @Json(name = "subtitle")
    val subtitle: String?,
    @Json(name = "reasons")
    val reasons: List<Reason>?,
    @Json(name = "template")
    val template: TemplateDialogModel? = null,
) {

    @JsonClass(generateAdapter = true)
    data class Reason(
        @Json(name = "code")
        val code: String,
        @Json(name = "name")
        val name: String,

        val isSelected: Boolean = false
    )
}
package ua.gov.diia.ps_criminal_cert.models.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import ua.gov.diia.ui_base.views.NameModel
import ua.gov.diia.core.models.common.message.AttentionMessage
import ua.gov.diia.ps_criminal_cert.models.enums.CriminalCertScreen
import ua.gov.diia.core.models.dialogs.TemplateDialogModel

@JsonClass(generateAdapter = true)
data class CriminalCertNationalities(
    @Json(name = "nationalitiesScreen")
    val data: NationalitiesScreen?,
    @Json(name = "template")
    val template: TemplateDialogModel? = null,

    val countryList: List<NameModel> = emptyList()
) {

    val showAttentionMessage = data?.attentionMessage != null
    val isNextAvailable = countryList.any { it.name.isNotBlank() }
    val canAdd = countryList.size < (data?.maxNationalitiesCount ?: 0)
        && countryList.any { it.name.uppercase() != "УКРАЇНА" }
        && countryList.any { it.name.isNotBlank() }

    @JsonClass(generateAdapter = true)
    data class NationalitiesScreen(
        @Json(name = "title")
        val title: String?,
        @Json(name = "attentionMessage")
        val attentionMessage: AttentionMessage?,
        @Json(name = "country")
        val country: Country?,
        @Json(name = "maxNationalitiesCount")
        val maxNationalitiesCount: Int?,
        @Json(name = "nextScreen")
        val nextScreen: CriminalCertScreen?
    )

    @JsonClass(generateAdapter = true)
    data class Country(
        @Json(name = "label")
        val label: String,
        @Json(name = "hint")
        val hint: String,
        @Json(name = "addAction")
        val addAction: Action?
    )

    @JsonClass(generateAdapter = true)
    data class Action(
        @Json(name = "icon")
        val icon: String,
        @Json(name = "name")
        val name: String
    )
}
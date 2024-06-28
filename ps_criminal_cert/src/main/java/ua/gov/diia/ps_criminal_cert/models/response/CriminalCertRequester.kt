package ua.gov.diia.ps_criminal_cert.models.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import ua.gov.diia.ui_base.views.NameModel
import ua.gov.diia.core.models.common.message.AttentionMessageMlc
import ua.gov.diia.ps_criminal_cert.models.enums.CriminalCertScreen
import ua.gov.diia.core.models.dialogs.TemplateDialogModel

@JsonClass(generateAdapter = true)
data class CriminalCertRequester(
    @Json(name = "requesterDataScreen")
    val requesterDataScreen: RequesterDataScreen?,
    @Json(name = "processCode")
    val processCode: Int?,
    @Json(name = "template")
    val template: TemplateDialogModel?,

    val prevFirstNameData: NameData = NameData(),
    val prevMiddleNameData: NameData = NameData(),
    val prevLastNameData: NameData = NameData()
) {

    val showAttentionMessage = requesterDataScreen?.attentionMessage != null

    @JsonClass(generateAdapter = true)
    data class RequesterDataScreen(
        @Json(name = "title")
        val title: String,
        @Json(name = "attentionMessage")
        val attentionMessage: AttentionMessageMlc?,
        @Json(name = "fullName")
        val fullName: Name,
        @Json(name = "nextScreen")
        val nextScreen: CriminalCertScreen,
    )

    @JsonClass(generateAdapter = true)
    data class Name(
        @Json(name = "label")
        val label: String,
        @Json(name = "value")
        val value: String
    )

    data class NameData(
        val list: List<NameModel> = emptyList()
    ) {
        val canAdd: Boolean = list.size < 10 && list.all { it.name.isNotBlank() }
    }
}
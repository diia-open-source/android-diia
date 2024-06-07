package ua.gov.diia.ps_criminal_cert.models.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import ua.gov.diia.core.models.common.message.AttentionMessageMlc
import ua.gov.diia.core.models.dialogs.TemplateDialogModel

@JsonClass(generateAdapter = true)
data class CriminalCertInfo(
    @Json(name = "showContextMenu")
    val showContextMenu: Boolean? = null,
    @Json(name = "title")
    val title: String? = null,
    @Json(name = "text")
    val text: String? = null,
    @Json(name = "attentionMessage")
    val attentionMessage: AttentionMessageMlc? = null,
    @Json(name = "template")
    val template: TemplateDialogModel? = null,
    @Json(name = "nextScreen")
    val nextScreen: String? = null,
)

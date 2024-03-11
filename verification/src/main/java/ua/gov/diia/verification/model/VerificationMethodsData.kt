package ua.gov.diia.verification.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import ua.gov.diia.core.models.dialogs.TemplateDialogModel
import ua.gov.diia.verification.model.ActivityViewActionButton

@JsonClass(generateAdapter = true)
data class VerificationMethodsData(
    @Json(name = "title")
    val title: String?,
    @Json(name = "authMethods")
    val methods: List<String>?,
    @Json(name = "button")
    val actionButton: ActivityViewActionButton?,
    @Json(name = "processId")
    val processId: String?,
    @Json(name = "skipAuthMethods")
    val skipAuthMethods: Boolean?,
    @Json(name = "template")
    val template: TemplateDialogModel?
)

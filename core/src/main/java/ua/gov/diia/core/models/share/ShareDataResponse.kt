package ua.gov.diia.core.models.share

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import ua.gov.diia.core.models.dialogs.TemplateDialogModel

@JsonClass(generateAdapter = true)
data class ShareDataResponse(
    @Json(name = "link")
    val link: String?,
    @Json(name = "ownerRequestScreen")
    val ownerRequestScreen: OwnerRequestScreen?,
    @Json(name = "processCode")
    val processCode: Int?,
    @Json(name = "template")
    val template: TemplateDialogModel?,
) {

    @JsonClass(generateAdapter = true)
    data class OwnerRequestScreen(
        @Json(name = "title")
        val title: String,
        @Json(name = "description")
        val description: String,
        @Json(name = "qr")
        val qr: Qr,
        @Json(name = "linkAction")
        val linkAction: LinkAction,
    )

    @JsonClass(generateAdapter = true)
    data class Qr(
        @Json(name = "ttl")
        val ttl: String,
        @Json(name = "value")
        val value: String,
    )

    @JsonClass(generateAdapter = true)
    data class LinkAction(
        @Json(name = "icon")
        val icon: String,
        @Json(name = "name")
        val name: String,
    )
}
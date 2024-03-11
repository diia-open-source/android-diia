package ua.gov.diia.ps_criminal_cert.models.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import ua.gov.diia.core.models.common.NavigationPanel
import ua.gov.diia.core.models.common.menu.ContextMenuItem
import ua.gov.diia.ps_criminal_cert.models.enums.CriminalCertLoadActionType
import ua.gov.diia.ps_criminal_cert.models.enums.CriminalCertStatus
import ua.gov.diia.core.models.rating_service.RatingFormModel

@JsonClass(generateAdapter = true)
data class CriminalCertDetails(
    @Json(name = "contextMenu")
    val contextMenu: List<ContextMenuItem>?,
    @Json(name = "title")
    val title: String,
    @Json(name = "status")
    val status: CriminalCertStatus,
    @Json(name = "statusMessage")
    val statusMessage: StatusMessage,
    @Json(name = "loadActions")
    val loadActions: List<LoadAction>?,
    @Json(name = "ratingForm")
    val ratingForm: RatingFormModel?,
    @Json(name = "navigationPanel")
    val navigationPanel: NavigationPanel?
) {

    @JsonClass(generateAdapter = true)
    data class StatusMessage(
        @Json(name = "title")
        val title: String?,
        @Json(name = "text")
        val text: String?,
        @Json(name = "icon")
        val icon: String?
    )

    @JsonClass(generateAdapter = true)
    data class LoadAction(
        @Json(name = "type")
        val type: CriminalCertLoadActionType,
        @Json(name = "icon")
        val icon: String?,
        @Json(name = "name")
        val name: String,

        val isLoading: Boolean = false,
        val isEnabled: Boolean = true
    )
}
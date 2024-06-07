package ua.gov.diia.core.models.common_compose.general


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import ua.gov.diia.core.models.common_compose.mlc.header.NavigationPanelMlc
import ua.gov.diia.core.models.common_compose.org.header.TopGroupOrg

@JsonClass(generateAdapter = true)
data class TopGroup(
    @Json(name = "topGroupOrg")
    val topGroupOrg: TopGroupOrg?,
    @Json(name = "navigationPanelMlc")
    val navigationPanelMlc: NavigationPanelMlc?
)
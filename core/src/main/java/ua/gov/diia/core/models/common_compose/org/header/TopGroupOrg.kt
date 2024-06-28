package ua.gov.diia.core.models.common_compose.org.header

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import ua.gov.diia.core.models.common_compose.mlc.header.NavigationPanelMlc
import ua.gov.diia.core.models.common_compose.mlc.header.TitleGroupMlc

@JsonClass(generateAdapter = true)
data class TopGroupOrg(
    @Json(name = "chipTabsOrg")
    val chipTabsOrg: ChipTabsOrg?,
    @Json(name = "navigationPanelMlc")
    val navigationPanelMlc: NavigationPanelMlc?,
    @Json(name = "titleGroupMlc")
    val titleGroupMlc: TitleGroupMlc?,
)
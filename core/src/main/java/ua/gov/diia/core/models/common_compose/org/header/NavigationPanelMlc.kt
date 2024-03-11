package ua.gov.diia.core.models.common_compose.org.header


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import ua.gov.diia.core.models.common.menu.ContextMenuItem

@JsonClass(generateAdapter = true)
data class NavigationPanelMlc(
    @Json(name = "ellipseMenu")
    val ellipseMenu: List<ContextMenuItem>?,
    @Json(name = "label")
    val label: String?
)
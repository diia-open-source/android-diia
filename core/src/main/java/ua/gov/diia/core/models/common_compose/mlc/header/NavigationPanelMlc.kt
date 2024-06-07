package ua.gov.diia.core.models.common_compose.mlc.header


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import ua.gov.diia.core.models.common.menu.ContextMenuItem
import ua.gov.diia.core.models.common_compose.atm.icon.IconAtm

@JsonClass(generateAdapter = true)
data class NavigationPanelMlc(
    @Json(name = "ellipseMenu")
    val ellipseMenu: List<ContextMenuItem>?,
    @Json(name = "label")
    val label: String?,
    @Json(name = "componentId")
    val componentId: String? = null,
    @Json(name = "iconAtm")
    val iconAtm: IconAtm? = null
)
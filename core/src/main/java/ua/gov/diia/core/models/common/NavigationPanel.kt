package ua.gov.diia.core.models.common

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import ua.gov.diia.core.models.ContextMenuField
import ua.gov.diia.core.models.common.menu.ContextMenuItem

@JsonClass(generateAdapter = true)
data class NavigationPanel(
    @Json(name = "header")
    val header: String?,
    @Json(name = "contextMenu")
    val contextMenu: List<ContextMenuItem>?,
) {
    val menu: Array<ContextMenuField>
        get() = contextMenu?.toTypedArray() ?: arrayOf()
}
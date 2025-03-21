package ua.gov.diia.core.models.common_compose.org.list

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import ua.gov.diia.core.models.common_compose.atm.button.BtnPlainIconAtm
import ua.gov.diia.core.models.common_compose.mlc.list.ListItemMlc

@JsonClass(generateAdapter = true)
data class ListItemEditGroupOrg(
    @Json(name = "componentId")
    val componentId: String?,
    @Json(name = "title")
    val title: String?,
    @Json(name = "description")
    val description: String?,
    @Json(name = "items")
    val items: List<ListItemMlc>?,
    @Json(name = "btnPlainIconAtm")
    val btnPlainIconAtm: BtnPlainIconAtm?
)
package ua.gov.diia.core.models.common_compose.org.media

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import ua.gov.diia.core.models.common_compose.atm.button.BtnPlainIconAtm
import ua.gov.diia.core.models.common_compose.mlc.list.ListItemMlc

@JsonClass(generateAdapter = true)
data class GroupFilesAddOrg(
    @Json(name = "componentId")
    val componentId: String?,
    @Json(name = "id")
    val id: String?,
    @Json(name = "items")
    val items: List<Item>?,
    @Json(name = "btnPlainIconAtm")
    val btnPlainIconAtm: BtnPlainIconAtm
)

@JsonClass(generateAdapter = true)
data class Item(
    @Json(name = "listItemMlc")
    val listItemMlc: ListItemMlc,
)
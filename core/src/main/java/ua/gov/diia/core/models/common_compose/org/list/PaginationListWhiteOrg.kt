package ua.gov.diia.core.models.common_compose.org.list

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import ua.gov.diia.core.models.common_compose.atm.text.GreyTitleAtm
import ua.gov.diia.core.models.common_compose.mlc.card.CardMlc
import ua.gov.diia.core.models.common_compose.mlc.list.ListItemMlc
import ua.gov.diia.core.models.common_compose.mlc.list.ListWidgetItemMlc

@JsonClass(generateAdapter = true)
data class PaginationListWhiteOrg(
    @Json(name = "componentId")
    val componentId: String? = null,
    @Json(name = "showDivider")
    val showDivider: Boolean? = null,
    @Json(name = "items")
    val items: List<PaginationItem>,
    @Json(name = "limit")
    val limit: Int?
)
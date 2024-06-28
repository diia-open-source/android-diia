package ua.gov.diia.core.models.common_compose.org.chip

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import ua.gov.diia.core.models.common_compose.mlc.chip.ChipTimeMlc
import ua.gov.diia.core.models.common_compose.org.header.ChipTabsOrg

@JsonClass(generateAdapter = true)
data class ChipGroupOrg(
    @Json(name = "preselectedCode")
    val preselectedCode: String? = null,
    @Json(name = "label")
    val label: String? = null,
    @Json(name = "componentId")
    val componentId: String? = null,
    @Json(name = "items")
    val items: List<Item>? = null,
) {
    @JsonClass(generateAdapter = true)
    data class Item(
        @Json(name = "chipMlc")
        val chipMlc: ChipTabsOrg.Item? = null,
        @Json(name = "chipTimeMlc")
        val chipTimeMlc: ChipTimeMlc? = null,
    )
}
package ua.gov.diia.core.models.common_compose.org.chip

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import ua.gov.diia.core.models.common_compose.mlc.chip.ChipMlc

@JsonClass(generateAdapter = true)
data class MapChipTabsOrg(
    @Json(name = "componentId")
    val componentId: String?,
    @Json(name = "label")
    val label: String?,
    @Json(name = "items")
    val items: List<ChipItem>,
    @Json(name = "preselectedCode")
    val preselectedCode: String
) {

    @JsonClass(generateAdapter = true)
    data class ChipItem(
        @Json(name = "mapChipMlc")
        val chipMlc: ChipMlc?
    )
}
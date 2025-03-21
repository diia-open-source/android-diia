package ua.gov.diia.core.models.common_compose.mlc.card


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import ua.gov.diia.core.models.common_compose.atm.chip.ChipStatusAtm
import ua.gov.diia.core.models.common_compose.atm.icon.IconUrlAtm
import ua.gov.diia.core.models.common_compose.atm.icon.SmallIconAtm
import ua.gov.diia.core.models.common_compose.general.Action

@JsonClass(generateAdapter = true)
data class CardMlcV2(
    @Json(name = "action")
    val action: Action?,
    @Json(name = "chipStatusAtm")
    val chipStatusAtm: ChipStatusAtm?,
    @Json(name = "chips")
    val chips: List<Chip>?,
    @Json(name = "componentId")
    val componentId: String?,
    @Json(name = "descriptions")
    val descriptions: List<String>?,
    @Json(name = "iconUrlAtm")
    val iconUrlAtm: IconUrlAtm?,
    @Json(name = "label")
    val label: String?,
    @Json(name = "smallIconAtm")
    val smallIconAtm: SmallIconAtm?
) {
    data class Chip(
        @Json(name = "chipStatusAtm")
        val chipStatusAtm: ChipStatusAtm?,
    )
}
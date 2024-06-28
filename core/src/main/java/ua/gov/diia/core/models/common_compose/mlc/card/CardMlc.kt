package ua.gov.diia.core.models.common_compose.mlc.card

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import ua.gov.diia.core.models.common_compose.atm.button.BtnPrimaryAdditionalAtm
import ua.gov.diia.core.models.common_compose.atm.button.BtnStrokeAdditionalAtm
import ua.gov.diia.core.models.common_compose.atm.chip.ChipStatusAtm
import ua.gov.diia.core.models.common_compose.atm.text.TickerAtm

@JsonClass(generateAdapter = true)
data class CardMlc(
    @Json(name = "componentId")
    val componentId: String? = null,
    @Json(name = "id")
    val id: String,
    @Json(name = "chipStatusAtm")
    val chipStatusAtm: ChipStatusAtm?,
    @Json(name = "label")
    val label: String?,
    @Json(name = "title")
    val title: String?,
    @Json(name = "subtitle")
    val subtitle: Subtitle?,
    @Json(name = "subtitles")
    val subtitles: List<Subtitle>?,
    @Json(name = "description")
    val description: String?,
    @Json(name = "botLabel")
    val botLabel: String?,
    @Json(name = "ticker")
    val ticker: TickerAtm?,
    @Json(name = "tickerAtm")
    val tickerAtm: TickerAtm?,
    @Json(name = "btnPrimaryAdditionalAtm")
    val btnPrimaryAdditionalAtm: BtnPrimaryAdditionalAtm?,
    @Json(name = "btnStrokeAdditionalAtm")
    val btnStrokeAdditionalAtm: BtnStrokeAdditionalAtm?,
)

@JsonClass(generateAdapter = true)
data class Subtitle(
    @Json(name = "icon")
    val icon: String?,
    @Json(name = "value")
    val value: String?,
)
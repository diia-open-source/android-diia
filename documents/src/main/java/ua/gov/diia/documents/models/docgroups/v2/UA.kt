package ua.gov.diia.documents.models.docgroups.v2


import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize
import ua.gov.diia.core.models.common_compose.atm.chip.ChipStatusAtm
import ua.gov.diia.core.models.common_compose.atm.text.TickerAtm
import ua.gov.diia.core.models.common_compose.mlc.text.SmallEmojiPanelMlc
import ua.gov.diia.core.models.common_compose.table.tableBlockTwoColumnsPlaneOrg.TableBlockTwoColumnsPlaneOrg
import ua.gov.diia.core.models.common_compose.table.tableBlockPlaneOrg.TableBlockPlaneOrg

@Parcelize
@JsonClass(generateAdapter = true)
data class UA(
    @Json(name = "docButtonHeadingOrg")
    val docButtonHeadingOrg: DocButtonHeadingOrg? = null,
    @Json(name = "docHeadingOrg")
    val docHeadingOrg: DocHeadingOrg? = null,
    @Json(name = "tableBlockTwoColumnsPlaneOrg")
    val tableBlockTwoColumnsPlaneOrg: TableBlockTwoColumnsPlaneOrg? = null,
    @Json(name = "tableBlockPlaneOrg")
    val tableBlockPlaneOrg: TableBlockPlaneOrg? = null,
    @Json(name = "tickerAtm")
    val tickerAtm: TickerAtm? = null,
    @Json(name = "smallEmojiPanelMlc")
    val smallEmojiPanelMlc: SmallEmojiPanelMlc? = null,
    @Json(name = "subtitleLabelMlc")
    val subtitleLabelMlc: SubtitleLabelMlc? = null,
    @Json(name = "chipStatusAtm")
    val chipStatusAtm: ChipStatusAtm? = null,
) : Parcelable
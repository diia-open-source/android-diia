package ua.gov.diia.documents.models.docgroups.v2


import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize
import ua.gov.diia.core.models.common_compose.atm.text.TickerAtm
import ua.gov.diia.core.models.common_compose.table.tableBlockOrg.TableBlockOrg
import ua.gov.diia.core.models.common_compose.table.tableBlockTwoColumnsOrg.TableBlockTwoColumnsOrg

@Parcelize
@JsonClass(generateAdapter = true)
data class FullInfo(
    @Json(name = "docHeadingOrg")
    val docHeadingOrg: DocHeadingOrg? = null,
    @Json(name = "tableBlockOrg")
    val tableBlockOrg: TableBlockOrg?  = null,
    @Json(name = "tableBlockTwoColumnsOrg")
    val tableBlockTwoColumnsOrg: TableBlockTwoColumnsOrg?  = null,
    @Json(name = "tickerAtm")
    val tickerAtm: TickerAtm?  = null
) : Parcelable
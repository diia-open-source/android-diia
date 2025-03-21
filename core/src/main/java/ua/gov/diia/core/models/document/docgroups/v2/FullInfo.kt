package ua.gov.diia.core.models.document.docgroups.v2


import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize
import ua.gov.diia.core.models.common_compose.atm.text.TickerAtm
import ua.gov.diia.core.models.common_compose.org.chip.ChipBlackGroupOrg
import ua.gov.diia.core.models.common_compose.org.doc.DocHeadingOrg
import ua.gov.diia.core.models.common_compose.org.list.ListItemGroupOrg
import ua.gov.diia.core.models.common_compose.org.verification.VerificationCodesOrg
import ua.gov.diia.core.models.common_compose.table.tableBlockAccordionOrg.TableBlockAccordionOrg
import ua.gov.diia.core.models.common_compose.table.tableBlockOrg.TableBlockOrg
import ua.gov.diia.core.models.common_compose.table.tableBlockTwoColumnsOrg.TableBlockTwoColumnsOrg

@Parcelize
@JsonClass(generateAdapter = true)
data class FullInfo(
    @Json(name = "docHeadingOrg")
    val docHeadingOrg: DocHeadingOrg? = null,
    @Json(name = "tableBlockOrg")
    val tableBlockOrg: TableBlockOrg?  = null,
    @Json(name = "listItemGroupOrg")
    val listItemGroupOrg: ListItemGroupOrg?  = null,
    @Json(name = "tableBlockTwoColumnsOrg")
    val tableBlockTwoColumnsOrg: TableBlockTwoColumnsOrg?  = null,
    @Json(name = "tableBlockAccordionOrg")
    val tableBlockAccordionOrg: TableBlockAccordionOrg?  = null,
    @Json(name = "verificationCodesOrg")
    val verificationCodesOrg: VerificationCodesOrg? = null,
    @Json(name = "tickerAtm")
    val tickerAtm: TickerAtm?  = null,
    @Json(name = "chipBlackGroupOrg")
    val chipBlackGroupOrg: ChipBlackGroupOrg? = null
) : Parcelable
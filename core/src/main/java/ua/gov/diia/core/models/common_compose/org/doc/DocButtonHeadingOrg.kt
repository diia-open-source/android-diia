package ua.gov.diia.core.models.common_compose.org.doc


import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize
import ua.gov.diia.core.models.common_compose.atm.icon.IconAtm
import ua.gov.diia.core.models.common_compose.mlc.doc.DocNumberCopyMlc
import ua.gov.diia.core.models.common_compose.table.HeadingWithSubtitlesMlc
import ua.gov.diia.core.models.document.docgroups.v2.StackMlc

@Parcelize
@JsonClass(generateAdapter = true)
data class DocButtonHeadingOrg(
    @Json(name = "componentId")
    val componentId: String? = null,
    @Json(name = "docNumberCopyMlc")
    val docNumberCopyMlc: DocNumberCopyMlc? = null,
    @Json(name = "headingWithSubtitlesMlc")
    val headingWithSubtitlesMlc: HeadingWithSubtitlesMlc? = null,
    @Json(name = "headingWithSubtitleWhiteMlc")
    val headingWithSubtitleWhiteMlc: HeadingWithSubtitlesMlc? = null,
    @Json(name = "stackMlc")
    val stackMlc: StackMlc? = null,
    @Json(name = "iconAtm")
    val iconAtm: IconAtm? = null,
    @Json(name = "docNumberCopyWhiteMlc")
    val docNumberCopyWhiteMlc: DocNumberCopyMlc? = null,
) : Parcelable
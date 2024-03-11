package ua.gov.diia.core.models.common_compose.table.tableBlockTwoColumnsPlaneOrg

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize
import ua.gov.diia.core.models.common_compose.table.HeadingWithSubtitlesMlc
import ua.gov.diia.core.models.common_compose.table.Item


@Parcelize
@JsonClass(generateAdapter = true)
data class TableBlockTwoColumnsPlaneOrg(
    @Json(name = "componentId")
    val componentId: String? = null,
    @Json(name = "items")
    val items: List<Item>? = null,
    @Json(name = "photo")
    val photo: String? = null,
    @Json(name = "headingWithSubtitlesMlc")
    val headingWithSubtitlesMlc: HeadingWithSubtitlesMlc? = null
): Parcelable
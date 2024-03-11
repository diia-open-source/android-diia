package ua.gov.diia.core.models.common_compose.table.tableBlockTwoColumnsOrg


import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize
import ua.gov.diia.core.models.common_compose.table.HeadingWithSubtitlesMlc
import ua.gov.diia.core.models.common_compose.table.Item

@Parcelize
@JsonClass(generateAdapter = true)
data class TableBlockTwoColumnsOrg(
    @Json(name = "headingWithSubtitlesMlc")
    val headingWithSubtitlesMlc: HeadingWithSubtitlesMlc?,
    @Json(name = "items")
    val items: List<Item>?,
    @Json(name = "photo")
    val photo: String?
) : Parcelable
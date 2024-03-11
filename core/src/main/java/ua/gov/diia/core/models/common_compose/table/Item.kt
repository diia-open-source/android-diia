package ua.gov.diia.core.models.common_compose.table


import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize
import ua.gov.diia.core.models.common_compose.mlc.text.SmallEmojiPanelMlc

@Parcelize
@JsonClass(generateAdapter = true)
data class Item(
    @Json(name = "docTableItemHorizontalLongerMlc")
    val docTableItemHorizontalLongerMlc: TableItemHorizontalMlc? = null,
    @Json(name = "docTableItemHorizontalMlc")
    val docTableItemHorizontalMlc: TableItemHorizontalMlc? = null,
    @Json(name = "tableItemHorizontalMlc")
    val tableItemHorizontalMlc: TableItemHorizontalMlc? = null,
    @Json(name = "tableItemPrimaryMlc")
    val tableItemPrimaryMlc: TableItemPrimaryMlc? = null,
    @Json(name = "tableItemVerticalMlc")
    val tableItemVerticalMlc: TableItemVerticalMlc? = null,
    @Json(name = "smallEmojiPanelMlc")
    val smallEmojiPanelMlc: SmallEmojiPanelMlc? = null,
) : Parcelable
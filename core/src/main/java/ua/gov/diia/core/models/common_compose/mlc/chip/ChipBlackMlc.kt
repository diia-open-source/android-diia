package ua.gov.diia.core.models.common_compose.mlc.chip

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@JsonClass(generateAdapter = true)
@Parcelize
data class ChipBlackMlcItem(
    @Json(name = "chipBlackMlc")
    val chipBlackMlc: ChipBlackMlc
) : Parcelable

@JsonClass(generateAdapter = true)
@Parcelize
data class ChipBlackMlc(
    @Json(name = "componentId")
    val componentId: String?,
    @Json(name = "label")
    val label: String,
    @Json(name = "code")
    val code: String,
    @Json(name = "active")
    val active: Boolean?
) : Parcelable
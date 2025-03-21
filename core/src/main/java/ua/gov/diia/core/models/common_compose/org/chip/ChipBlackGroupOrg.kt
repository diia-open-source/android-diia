package ua.gov.diia.core.models.common_compose.org.chip

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize
import ua.gov.diia.core.models.common_compose.mlc.chip.ChipBlackMlcItem

@Parcelize
@JsonClass(generateAdapter = true)
data class ChipBlackGroupOrg(
    @Json(name = "componentId")
    val componentId: String?,
    @Json(name = "id")
    val id: String?,
    @Json(name = "inputCode")
    val inputCode: String?,
    @Json(name = "mandatory")
    val mandatory: Boolean?,
    @Json(name = "label")
    val label: String?,
    @Json(name = "minCount")
    val minCount: Int?,
    @Json(name = "maxCount")
    val maxCount: Int?,
    @Json(name = "items")
    val items: List<ChipBlackMlcItem>,
    @Json(name = "preselectedCodes")
    val preselectedCodes: List<String>?
): Parcelable
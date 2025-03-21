package ua.gov.diia.core.models.common_compose.org.card


import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class DashboardCardTileOrg(
    @Json(name = "componentId")
    val componentId: String?,
    @Json(name = "items")
    val items: List<Item>?
): Parcelable
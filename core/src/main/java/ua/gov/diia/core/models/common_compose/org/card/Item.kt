package ua.gov.diia.core.models.common_compose.org.card


import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize
import ua.gov.diia.core.models.common_compose.mlc.card.DashboardCardMlc
@Parcelize
@JsonClass(generateAdapter = true)
data class Item(
    @Json(name = "dashboardCardMlc")
    val dashboardCardMlc: DashboardCardMlc?
): Parcelable
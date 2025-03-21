package ua.gov.diia.core.models.common_compose.atm.text

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize
import ua.gov.diia.core.models.common_compose.general.Action

@Parcelize
@JsonClass(generateAdapter = true)
data class TickerAtm(
    @Json(name = "componentId")
    val componentId: String? = null,
    @Json(name = "action")
    val action: Action? = null,
    @Json(name = "type")
    val type: TickerType,
    @Json(name = "usage")
    val usage: UsageType?,
    @Json(name = "value")
    val value: String
) : Parcelable {
    enum class TickerType {
        warning, positive, neutral, informative, negative, pink, rainbow, blue;
    }

    enum class UsageType {
        document, stackedCard, base, grand;
    }
}
package ua.gov.diia.core.models.common_compose.atm.text


import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize
import ua.gov.diia.core.models.common_compose.general.Action

@Parcelize
@JsonClass(generateAdapter = true)
data class LargeTickerAtm(
    @Json(name = "action")
    val action: Action? = null,
    @Json(name = "componentId")
    val componentId: String? = null,
    @Json(name = "type")
    val type: TickerType,
    @Json(name = "usage")
    val usage: String?,
    @Json(name = "value")
    val value: String?
) : Parcelable {
    enum class TickerType {
        informative, blue, pink, rainbow,  warning, negative, positive, neutral, cyan;
    }
}
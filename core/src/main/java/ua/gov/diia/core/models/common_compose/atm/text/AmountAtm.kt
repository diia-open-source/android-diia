package ua.gov.diia.core.models.common_compose.atm.text


import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class AmountAtm(
    @Json(name = "colour")
    val colour: Color,
    @Json(name = "componentId")
    val componentId: String?,
    @Json(name = "value")
    val value: String?
) : Parcelable {
    enum class Color {
        black, green, red
    }
}
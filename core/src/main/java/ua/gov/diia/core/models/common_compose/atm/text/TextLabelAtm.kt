package ua.gov.diia.core.models.common_compose.atm.text

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class TextLabelAtm(
    @Json(name = "componentId")
    val componentId: String? = null,
    @Json(name = "code")
    val mode: Mode,
    @Json(name = "label")
    val label: String,
    @Json(name = "value")
    val value: String?
) : Parcelable {
    enum class Mode(val type: String) {
        PRIMARY("primary"),
        SECONDARY("secondary")
    }
}
package ua.gov.diia.core.models.common_compose.atm.text

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class SlideBarAtm(
    @Json(name = "componentId")
    val componentId: String?,
    @Json(name = "type")
    val type: SlideBarType,
    @Json(name = "value")
    val value: String?
) : Parcelable {

    enum class SlideBarType {
        success, attention;
    }
}
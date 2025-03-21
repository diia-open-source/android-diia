package ua.gov.diia.core.models.common_compose.atm.icon


import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize
import ua.gov.diia.core.models.common_compose.general.Action

@Parcelize
@JsonClass(generateAdapter = true)
data class IconAtm(
    @Json(name = "componentId")
    val componentId: String? = null,
    @Json(name = "accessibilityDescription")
    val accessibilityDescription: String? = null,
    @Json(name = "action")
    val action: Action?,
    @Json(name = "code")
    val code: String,
    @Json(name = "isEnable")
    val isEnable: Boolean? = null
) : Parcelable
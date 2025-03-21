package ua.gov.diia.core.models.common_compose.atm.media

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize
import ua.gov.diia.core.models.common_compose.general.Action
@Parcelize
@JsonClass(generateAdapter = true)
data class PlayerBtnAtm(
    @Json(name = "type")
    val type: String,
    @Json(name = "icon")
    val icon: String,
    @Json(name = "action")
    val action: Action?,
): Parcelable
package ua.gov.diia.core.models.common_compose.atm.icon

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize
import ua.gov.diia.core.models.common_compose.general.Action

@Parcelize
@JsonClass(generateAdapter = true)
data class UserPictureAtm(
    @Json(name = "action")
    val action: Action?,
    @Json(name = "componentId")
    val componentId: String?,
    @Json(name = "defaultImageCode")
    val defaultImageCode: DefaultImageCode?,
    @Json(name = "useDocPhoto")
    val useDocPhoto: Boolean?
) : Parcelable {
    enum class DefaultImageCode {
        userFemale, userMale;
    }
}
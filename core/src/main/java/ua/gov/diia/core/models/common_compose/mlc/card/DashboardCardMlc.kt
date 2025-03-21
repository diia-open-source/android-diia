package ua.gov.diia.core.models.common_compose.mlc.card


import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize
import ua.gov.diia.core.models.common_compose.atm.button.BtnSemiLightAtm
import ua.gov.diia.core.models.common_compose.general.Action
@Parcelize
@JsonClass(generateAdapter = true)
data class DashboardCardMlc(
    @Json(name = "action")
    val action: Action?,
    @Json(name = "amountLarge")
    val valueLarge: String?,
    @Json(name = "amountSmall")
    val valueSmall: String?,
    @Json(name = "btnSemiLightAtm")
    val btnSemiLightAtm: BtnSemiLightAtm?,
    @Json(name = "componentId")
    val componentId: String?,
    @Json(name = "description")
    val description: String?,
    @Json(name = "descriptionCenter")
    val descriptionCenter: String?,
    @Json(name = "icon")
    val icon: String?,
    @Json(name = "iconCenter")
    val iconCenter: String?,
    @Json(name = "label")
    val label: String?,
    @Json(name = "type")
    val type: DashboardCardType
): Parcelable {
    enum class DashboardCardType {
        description, button, empty;
    }
}
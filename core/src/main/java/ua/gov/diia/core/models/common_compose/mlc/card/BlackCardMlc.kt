package ua.gov.diia.core.models.common_compose.mlc.card


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import ua.gov.diia.core.models.common_compose.atm.icon.DoubleIconAtm
import ua.gov.diia.core.models.common_compose.atm.icon.IconAtm
import ua.gov.diia.core.models.common_compose.atm.icon.SmallIconAtm
import ua.gov.diia.core.models.common_compose.general.Action

@JsonClass(generateAdapter = true)
data class BlackCardMlc(
    @Json(name = "action")
    val action: Action?,
    @Json(name = "doubleIconAtm")
    val doubleIconAtm: DoubleIconAtm?,
    @Json(name = "iconAtm")
    val iconAtm: IconAtm?,
    @Json(name = "label")
    val label: String?,
    @Json(name = "smallIconAtm")
    val smallIconAtm: SmallIconAtm?,
    @Json(name = "title")
    val title: String?
)